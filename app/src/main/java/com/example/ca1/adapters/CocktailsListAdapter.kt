package com.example.ca1.adapters

import android.content.Context
import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.example.ca1.databinding.ListItemBinding
import com.example.ca1.model.Cocktail
import com.bumptech.glide.Glide
import com.example.ca1.R
import com.example.ca1.data.FavouriteEntity

class CocktailsListAdapter(
    private val cocktailsList: List<Cocktail>?,
    private var favouritesList: MutableList<FavouriteEntity?>?,
    private val listener: ListItemListener,

    ) :

    RecyclerView.Adapter<CocktailsListAdapter.ViewHolder>() {
    var favourite: FavouriteEntity? = null
    var isFavourite: Boolean = false
    private lateinit var context: Context
    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val binding = ListItemBinding.bind(itemView)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context

        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = cocktailsList!!.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cocktail = cocktailsList?.get(holder.adapterPosition)

        if(!favouritesList?.isEmpty()!! && cocktail != null){
            favourite = getFavourite(cocktail.idDrink)
        }

        // Setting a custom font
       val myCustomFont : Typeface? = ResourcesCompat.getFont(context, R.font.comfortaa)
        holder.binding.cocktailText.typeface = myCustomFont

        val circularProgressDrawable = CircularProgressDrawable(holder.itemView.context)
        circularProgressDrawable.apply {
            strokeWidth = 5f
            centerRadius = 30f
            start()
        }

        with(holder.binding) {
            if (cocktail != null) {
                Glide.with(root).load(cocktail.strDrinkThumb).placeholder(circularProgressDrawable).centerCrop().into(imageView)
            }
            if (cocktail != null) {
                cocktailText.text = cocktail.strDrink

            }
            if (cocktail != null) {
                if (cocktail.strAlcoholic=="Alcoholic"){
                    holder.binding.checkBox.isChecked=true
                }
            }
            root.setOnClickListener{
                if (cocktail != null) {
                    listener.onItemClick(cocktail.idDrink, cocktail.strDrink, cocktail.strInstructions, cocktail.strDrinkThumb, "mainFragment")
                }
            }
            favouriteToggle.isChecked = favourite != null

            favouriteToggle.setOnClickListener{
                if (cocktail != null) {
                    listener.onSaveClick(cocktail, isFavourite, favourite?.id, position)
                }
            }
        }
    }

    private fun getFavourite(id: Int): FavouriteEntity?{
        return favouritesList?.find{ it?.id == id}
    }

    interface ListItemListener {
        fun onItemClick(cocktailId: Int, cocktailName: String, cocktailInstructions: String, cocktailImage: String, fragmentName: String)
        fun onSaveClick(cocktail: Cocktail, isFavourite: Boolean, adapterFavouriteId: Int?, position: Int)
    }
}