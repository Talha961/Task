package com.example.ca1.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ca1.viewModel.FavouritesViewModel
import com.example.ca1.adapters.FavouritesListAdapter
import com.example.ca1.data.FavouriteEntity
import com.example.ca1.databinding.FavouritesFragmentBinding

class FavouritesFragment : Fragment(),
    FavouritesListAdapter.ListItemListener{
    companion object {
        fun newInstance() = FavouritesFragment()
    }

    private lateinit var viewModel: FavouritesViewModel
    private lateinit var binding: FavouritesFragmentBinding
    private lateinit var adapter: FavouritesListAdapter
    private lateinit var spinner: ProgressBar
    var favouriteItems: MutableList<FavouriteEntity?>? = null

    override fun onResume() {
        super.onResume()
        favouriteItems = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        binding = FavouritesFragmentBinding.inflate(inflater, container, false)
        spinner = binding.progressBar1

        viewModel = ViewModelProvider(this).get(FavouritesViewModel::class.java)

        with(binding.favouritesRecyclerView){
            setHasFixedSize(true)
            val divider = DividerItemDecoration(
                context, LinearLayoutManager(context).orientation
            )
        }

        viewModel.favourites.observe(viewLifecycleOwner, Observer{
            if(viewModel.favourites.value != null){
                if (it != null) {
                    if(it.isNotEmpty()) {
                        this.favouriteItems = it
                        spinner.visibility = View.GONE;
                        if (favouriteItems != null) {
                            viewModel.favourites
                            adapter = FavouritesListAdapter(favouriteItems, this@FavouritesFragment)
                            binding.favouritesRecyclerView.adapter = adapter
                            binding.favouritesRecyclerView.layoutManager = LinearLayoutManager(activity)

                        }
                    }
                    else{
                        spinner.visibility = View.GONE
                        binding.noFavouritesSaved.visibility = View.VISIBLE
                    }
                }

            }
        })

        return binding.root
    }

    override fun onItemClick(cocktailId: Int, cocktailInstructions: String, cocktailName: String, cocktailImage: String, fragmentName: String) {
    }

    override fun onSaveClick(favourite: FavouriteEntity, isFavourite: Boolean, adapterFavouriteId: Int?, position: Int) {
        Log.i("FavouriteExistence", "Removing favourite: ${favourite.id} ${ favourite.strDrink} / adapterfavourite: $adapterFavouriteId")


        if(binding.favouritesRecyclerView.childCount == position){
            favouriteItems?.remove(favourite)
            viewModel.removeFavourite(favourite)

            adapter.notifyItemRemoved(position);
        }
        else {

            favouriteItems?.remove(favourite)
            viewModel.removeFavourite(favourite)

            adapter.notifyItemRemoved(position);
        }
        if(favouriteItems?.isEmpty() == true){
            binding.noFavouritesSaved.visibility = View.VISIBLE
        }
    }
}