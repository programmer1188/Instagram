package com.example.instagram.fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.instagram.MainActivity
import com.example.instagram.Post
import com.example.instagram.PostAdapter
import com.example.instagram.R
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery


open class FeedFragment : Fragment() {

    lateinit var postRecyclerView: RecyclerView

    lateinit var adapter: PostAdapter

    var allPosts: MutableList<Post> = mutableListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_feed, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //This is where we setup our views and click Listeners

        postRecyclerView =  view.findViewById(R.id.postRecyclerView)

        //Steps to populate Recycler View
        //1. Create Layout for each row in a list(using item_post.xml)
        //2. Create data source for each row(in Post class)
        //3. Create adapter that will bridge data and row layout (PostAdapter)
        //4. Set adapter on RecyclerView
        adapter = PostAdapter(requireContext(), allPosts)
        postRecyclerView.adapter = adapter

        //5. Set layout manager on Recycler View
        postRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        queryPosts()

    }

    //Query for all posts in our server
    open fun queryPosts() {

        // Specify which class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        //Find all Post objects
        query.include(Post.KEY_USER)
        //Return posts in descending order: ie newer posts will appear first
        query.findInBackground(object : FindCallback<Post> {
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                if(e != null){
                    //something has went wrong
                    Log.e(TAG,"Error fetching posts")
                }else{
                    if(posts != null){
                        for(post in posts){
                            Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser()?.username)
                        }

                        allPosts.addAll(posts)
                        adapter.notifyDataSetChanged()
                    }
                }
            }

        })
    }

    companion object {
        const val TAG = "FeedFragment"
    }

}