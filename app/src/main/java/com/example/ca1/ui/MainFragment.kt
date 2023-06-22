package com.example.ca1.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.ProgressBar
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ca1.viewModel.MainViewModel
import com.example.ca1.adapters.CocktailsListAdapter
import com.example.ca1.databinding.MainFragmentBinding
import com.example.ca1.data.FavouriteEntity
import com.example.ca1.data.MergedData
import com.example.ca1.model.Cocktail
import com.example.ca1.prefrance.PrefUtils

class MainFragment : Fragment(),
    CocktailsListAdapter.ListItemListener{
    private lateinit var viewModel: MainViewModel
    private lateinit var searchQuery: String
    private lateinit var radioGroup: RadioGroup
    private lateinit var binding: MainFragmentBinding
    private lateinit var adapter: CocktailsListAdapter
    private val args: MainFragmentArgs by navArgs()
    private lateinit var spinner: ProgressBar
    var cocktailItems: List<Cocktail>? = null
    var favouriteItems: MutableList<FavouriteEntity?>? = null
    private lateinit var responseJson: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)

        binding = MainFragmentBinding.inflate(inflater, container, false)
        spinner = binding.progressBar1
        if (isInternetAvailable()) {
            searchQuery = "margarita"
            val searchView = binding.searchView;
            radioGroup = binding.radioGroup
            if (context?.let { PrefUtils.getBoolean(it, "byname") } == true) {
                binding.radioButton1.isChecked = true
            }
            if (context?.let { PrefUtils.getBoolean(it, "byalphabet") } == true) {
                binding.radioButton2.isChecked = true
            }
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?): Boolean {
                    if (radioGroup.checkedRadioButtonId == binding.radioButton1.id) {
                        context?.let { PrefUtils.setBoolean(it, "byname", true) }
                        searchQuery = p0.toString()
                        viewModel.getCocktails(searchQuery, true)
                    } else {
                        context?.let { PrefUtils.setBoolean(it, "byalphabet", true) }
                        searchQuery = p0.toString()
                        viewModel.getCocktails(searchQuery, false)
                    }
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    Log.i("Search text:", "$newText")
                    return false
                }
            })
            viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
            val liveData = viewModel.fetchData()
            viewModel.getCocktails(searchQuery, true)

            with(binding.mainRecyclerView) {
                setHasFixedSize(true)
                val divider = DividerItemDecoration(
                    context, LinearLayoutManager(context).orientation
                )
            }

            liveData.observe(
                viewLifecycleOwner
            ) { it ->
                when (it) {
                    is MergedData.CocktailData -> cocktailItems = it.cocktailItems
                    is MergedData.FavouriteData -> favouriteItems = it.favouriteItems
                }

                if (cocktailItems?.isNotEmpty() == true) {
                    if (cocktailItems != null && favouriteItems != null) {

                        adapter =
                            CocktailsListAdapter(cocktailItems, favouriteItems, this@MainFragment)
                        binding.mainRecyclerView.adapter = adapter
                        binding.mainRecyclerView.layoutManager = LinearLayoutManager(activity)
                    }
                } else {
                    binding.noCocktailsFound.visibility = View.VISIBLE
                }

                if (it == null) {
                    spinner.visibility = View.VISIBLE;
                } else {
                    spinner.visibility = View.GONE;
                }
            }
        }
        return binding.root
    }

    override fun onItemClick(cocktailId: Int, cocktailName: String, cocktailInstructions: String, cocktailImage: String, fragmentName: String) {
    }

    override fun onSaveClick(cocktail: Cocktail, isFavourite: Boolean, adapterFavouriteId: Int?, position: Int) {

        if(favouriteItems?.contains(FavouriteEntity(cocktail.idDrink, cocktail.strDrink, cocktail.strInstructions,cocktail.strAlcoholic, cocktail.strDrinkThumb)) == true){
            Log.i("FavouriteExistence", "Cocktail already exists, unsaving : ${cocktail.idDrink} / adapterfavourite: $adapterFavouriteId")
            favouriteItems?.remove(FavouriteEntity(cocktail.idDrink, cocktail.strDrink, cocktail.strInstructions,cocktail.strAlcoholic, cocktail.strDrinkThumb))
            viewModel.removeFavourite(FavouriteEntity(cocktail.idDrink, cocktail.strDrink, cocktail.strInstructions,cocktail.strAlcoholic, cocktail.strDrinkThumb))
            adapter = CocktailsListAdapter(cocktailItems,favouriteItems, this@MainFragment)

        }
        else{
            Log.i("FavouriteExistence", "Cocktail does not already exist, saving: ${cocktail.idDrink} / adapterfavourite: $adapterFavouriteId")
            favouriteItems?.add(FavouriteEntity(cocktail.idDrink,cocktail.strDrink, cocktail.strInstructions,cocktail.strAlcoholic,cocktail.strDrinkThumb))
            viewModel.saveFavourite(FavouriteEntity(cocktail.idDrink, cocktail.strDrink, cocktail.strInstructions,cocktail.strAlcoholic, cocktail.strDrinkThumb))
            adapter = CocktailsListAdapter(cocktailItems,favouriteItems, this@MainFragment)

        }
    }
    private fun isInternetAvailable(): Boolean {
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)

        return networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            ?: false
    }
}