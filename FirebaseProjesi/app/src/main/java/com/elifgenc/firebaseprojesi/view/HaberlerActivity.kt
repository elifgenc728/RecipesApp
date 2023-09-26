package com.elifgenc.firebaseprojesi.view

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.elifgenc.firebaseprojesi.R
import com.elifgenc.firebaseprojesi.adapter.HaberRecyclerAdapter
import com.elifgenc.firebaseprojesi.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_haberler.*


class HaberlerActivity : AppCompatActivity() {
    private lateinit var auth : FirebaseAuth
    private lateinit var database :FirebaseFirestore

 //HaberRecyclerAdapter sınıfının postListesi parametresini alıp aşağıda kullanabilmek için bu parametreyi kullanıyoruz.
    private lateinit var recyclerViewAdapter : HaberRecyclerAdapter

    //Oluşturduğumuz Post isimli sınıftan liste olarak çekeceğiz.
    var postListesi = ArrayList<Post>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_haberler)

     auth = FirebaseAuth.getInstance()
     database = FirebaseFirestore.getInstance()

       verileriAl()

        //ADAPTER'İ BAĞLAMA
  // son olarak verileri gösterebilmemiz için yazdığımız adapteri buraya tanımlıyoruz.
    //LinearLayoutManager kullanacağımızı söylüyoruz alt alta recycler row'ların oluşturulacağnı söylemek için.
        var layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager =layoutManager

        recyclerViewAdapter = HaberRecyclerAdapter(postListesi)
        recyclerView.adapter = recyclerViewAdapter

    }
fun verileriAl() {
    //VERİ ÇEKME(Ekranda gösterme)
//aşağıdaki gibi belli bir document çağırmak istersek diye örnek yazdım
// database.collection("Post").document("").get()

    //aşağıdaki whereEqualTo kodu ile maili sadece elif@gmail.com olanların Post'unu getiriyoruz.
    //yani where ile filtreleme yapabiliyoruz.
 //database.collection("Post").whereEqualTo("kullaniciEmail","elif@gmail.com")

    //orderBy ile de belli bir sıraya göre çağırma yapabilyoruz.
database.collection("Post").orderBy("tarih", Query.Direction.DESCENDING).addSnapshotListener { snapshot, e ->
    if (e != null){
        Toast.makeText(this,e.localizedMessage,Toast.LENGTH_LONG).show()
    }else{
        if (snapshot != null){
            if (!snapshot.isEmpty){//boş değilse anlamı katar
                val documents = snapshot.documents

                //postListesi'nde bir şey kaldıysa temizliyor.
                postListesi.clear()

         for (document in documents){
               val kullaniciEmail= document.get("kullaniciEmail") as? String
               val kullaniciYorumu = document.get("kullaniciYorumu") as? String
               val gorselUrl = document.get("gorselurl") as? String

        //bu for döngüsünde document diyerek çektiğimiz verileri bir listede tutmamız gerekiyor,
        //işte burada Model dediğimiz yapı ortaya cıkıyor.Gelen veriler modelde tutuluyor ve biz de burada çağırabiliyoruz.

            //artık verileri kendi sınıfında kaydediyoruz.
             val indirilenPost = Post(kullaniciEmail,kullaniciYorumu,gorselUrl)
             postListesi.add(indirilenPost)
                }
                //döngüden sonra, yeni veri geldi kendini yenile diyoruz bu kodla
              recyclerViewAdapter.notifyDataSetChanged()
            }
        }
    }
}

}

//menu' yü kullanabilmek için iki tane fonksiyonu override etmemiz gerekiyor.
    // onCreateOptionsMenu ile menu' yü bağlıyouz.
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    //inflater, bağlama işlemi yapar.
    val menuInflater = menuInflater
        menuInflater.inflate(R.menu.option_menu,menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        //hangi item seçildiyse onun kontrolü yapılıyor.
        if (item.itemId == R.id.fotograf_paylas){
            //fotoğraf paylaşma aktivitesine gidilecek
            val intent = Intent(this, FotografPaylasmaActivity::class.java)
            startActivity(intent)
            //finish() yapmadık, kullanıcı geri dönmek isteyebilir sayfadan.
        }
        else if (item.itemId == R.id.cikis_yap)
        {
   //Firebaseden de çıkış yapmalısın bu yüzden onCreate içinde auth değişkeni oluşturup
   //Frebase'e bağladın ve şu an çıkışı oradan da yapacak kodu yazıyorsun.
            auth.signOut()
            val intent = Intent(this, CurrentActivity::class.java)
            startActivity(intent)
            finish()
        }

        return super.onOptionsItemSelected(item)
    }


}