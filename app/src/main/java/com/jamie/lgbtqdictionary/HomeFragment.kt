package com.jamie.lgbtqdictionary

import androidx.fragment.app.Fragment


class HomeFragment : Fragment(R.layout.fragment_home) {

//    lateinit var categoriesRV: RecyclerView
//    lateinit var thisContext: Context
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        thisContext = activity!!.applicationContext
//    }
//
//    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
//        super.onCreateView(inflater, container, savedInstanceState)
//        val view = inflater.inflate(R.layout.fragment_home, container, false)
//        categoriesRV = view.findViewById(R.id.rvCategories)
//        categoriesRV.setHasFixedSize(true)
//        categoriesRV.layoutManager = LinearLayoutManager(this.context)
//        showCategories()
//        return view
//
//    }
//
////    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
////        super.onViewCreated(view, savedInstanceState)
////
////
////        categoriesRV = view.findViewById(R.id.rvCategories)
////        categoriesRV.setHasFixedSize(true)
////        categoriesRV.layoutManager = LinearLayoutManager(this.context)
////        showCategories()
////    }
//
//    private fun showCategories() {
//        val dbRef = FirebaseDatabase.getInstance().getReference("categories")
//        val storageRef = FirebaseStorage.getInstance().reference
//        Log.i("DB.REF", dbRef.toString())
//
//        val options = FirebaseRecyclerOptions.Builder<Categories>()
//            .setQuery(dbRef, Categories::class.java)
//            .build()
//        Log.i("DB.OPTIONS", options.toString())
//
//        val adapter = object : FirebaseRecyclerAdapter<Categories, CategoriesViewHolder>(options) {
//            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
//                val view = LayoutInflater.from(parent.context).inflate(
//                    R.layout.categories_list_layout,
//                    parent,
//                    false
//                )
//                Log.i("onCreateViewHolder", view.toString())
//
//                return CategoriesViewHolder(view)
//            }
//
//            override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int, model: Categories) {
//                Log.i("MODEL", model.title + '-' + model.cover_img)
//                val url = "https://images.theconversation.com/files/161211/original/image-20170316-10898-1jrtrw4.jpg?ixlib=rb-1.1.0&rect=0%2C977%2C5306%2C2573&q=45&auto=format&w=1356&h=668&fit=crop"
//
//                storageRef.child(model.cover_img).downloadUrl.addOnSuccessListener {
//                    Log.i("COVER_URI", it.toString())
//                    holder.setDetails(model.title, it.toString(), thisContext)
//                }.addOnFailureListener{
//                    holder.setDetails(model.title, url, thisContext)
//                }
//            }
//
//        }
//
//        categoriesRV.adapter = adapter
//        adapter.startListening()
//    }
//
//    class CategoriesViewHolder(itemVIew : View) : RecyclerView.ViewHolder(itemVIew) {
//
//        fun setDetails(title : String, cover: String, context: Context) {
//            val cat_title = itemView.findViewById<TextView>(R.id.tvTitle)
//            val cat_cover = itemView.findViewById<ImageView>(R.id.ivCover)
//
//            cat_title.text = title
//            Glide.with(context).load(cover).into(cat_cover)
////            Glide.with(context).load(cover).into(cat_cover)
//
//        }
//
//    }

}