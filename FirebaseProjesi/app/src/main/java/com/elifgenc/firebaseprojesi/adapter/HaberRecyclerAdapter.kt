package com.elifgenc.firebaseprojesi.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elifgenc.firebaseprojesi.R
import com.elifgenc.firebaseprojesi.model.Post
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_row.view.*

//adapter görüntü ile bağlantı kurmamızı sağar.
//ViewHolder görünüm tutucudur.
//postList değişkeni Post modelini ArrayList olarak getiriyor.
class HaberRecyclerAdapter(val postList : ArrayList<Post>): RecyclerView.Adapter<HaberRecyclerAdapter.PostHolder>() {
 class PostHolder(itemView: View) :RecyclerView.ViewHolder(itemView){

 }

 override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostHolder {
 //inflater kullanarak oluşturduğumuz recycler_row'u burada bağlıyoruz.
  //layout bağladığmız için LayotInflater kullanıyoruz.
  //aşağıda context'i parent yani onCreateViewHolder'ın context'i oluyor.
  //en sonunda da PstHolder döndürüyoruz
  val inflater = LayoutInflater.from(parent.context)
  val view= inflater.inflate(R.layout.recycler_row,parent,false)
  return PostHolder(view)
 }

 override fun onBindViewHolder(holder: PostHolder, position: Int) {
//holder parametresi ile PostHolder sınıfından oluşurulan objeye ulaşabiliyoruz.
  holder.itemView.recycler_row_kullanici_email.text = postList[position].kullaniciEmail
  holder.itemView.recycler_row_kullanici_yorumu.text = postList[position].kullaniciYorumu
 //eklediğimiz Picasso kütüphnesi sayesinde görsel de çekebiliyoruz.
  Picasso.get().load(postList[position].gorselUrl).into(holder.itemView.recycler_row_imageview)
 }

 override fun getItemCount(): Int {
 return postList.size
 }
}