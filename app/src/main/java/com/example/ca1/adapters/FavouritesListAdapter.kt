package com.example.ca1.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.ca1.databinding.ListItemBinding
// we use this library for parsing images
import com.bumptech.glide.Glide
import com.example.ca1.R
import com.example.ca1.data.FavouriteEntity

class FavouritesListAdapter(
    private var favouritesList: MutableList<FavouriteEntity?>?,
    private val listener: ListItemListener
) :
    RecyclerView.Adapter<FavouritesListAdapter.ViewHolder>() {
    var favourite: FavouriteEntity? = null
    var isFavourite: Boolean = false

    inner class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val binding = ListItemBinding.bind(itemView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = favouritesList!!.size
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favourite = favouritesList?.get(holder.adapterPosition)


        with(holder.binding) {
            if (favourite != null) {
                Glide.with(root).load(favourite.strDrinkThumb).centerCrop().into(imageView)
            }
            if (favourite != null) {
                cocktailText.text = favourite.strDrink
            }

            favouriteToggle.isChecked = favourite != null
            if (favourite != null) {
                if (favourite.strAlcoholic=="Alcoholic"){
                    holder.binding.checkBox.isChecked=true
                }
            }
            root.setOnClickListener{
                if (favourite != null) {
                    listener.onItemClick(favourite.id, favourite.strDrink, favourite.strInstructions, favourite.strDrinkThumb, "favouritesFragment")
                }
            }


            favouriteToggle.setOnClickListener{
                    if (favourite != null) {
                        listener.onSaveClick(favourite, isFavourite, favourite.id, holder.layoutPosition)
                    }
            }
        }
    }

    fun setFavourites(newFavourites: MutableList<FavouriteEntity?>) {
        favouritesList = newFavourites
    }

    fun getFavourite(id: Int): FavouriteEntity?{
        return favouritesList?.find{ it?.id == id}
    }

    fun removeFavourite(id: Int){
        favouritesList?.remove(getFavourite(id))
    }

    fun addFavourite(favourite: FavouriteEntity){
        favouritesList?.add(favourite)
    }

    interface ListItemListener {
        fun onItemClick(cocktailId: Int, cocktailName: String, cocktailInstructions: String, cocktailImage: String, fragmentName: String)
        fun onSaveClick(favourite: FavouriteEntity, isFavourite: Boolean, adapterFavouriteId: Int?, position: Int)
    }
}