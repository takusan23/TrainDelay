package io.github.takusan23.traindelay

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_train_delay_layout.*
import okhttp3.*
import org.json.JSONArray
import java.io.IOException

class TrainDelayFragment : Fragment() {

    lateinit var recyclerViewList: ArrayList<ArrayList<*>>
    lateinit var trainDelayRecyclerViewAdapter: TrainDelayRecyclerViewAdapter
    lateinit var recyclerViewLayoutManager: RecyclerView.LayoutManager
    var isLoading = false

    //URL
    val trainDelayAPILink = "https://tetsudo.rti-giken.jp/free/delay.json"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_train_delay_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewList = ArrayList()
        //ここから下三行必須
        train_delay_recyclerview.setHasFixedSize(true)
        val mLayoutManager = LinearLayoutManager(context)
        train_delay_recyclerview.layoutManager = mLayoutManager as RecyclerView.LayoutManager?
        trainDelayRecyclerViewAdapter = TrainDelayRecyclerViewAdapter(recyclerViewList)
        train_delay_recyclerview.adapter = trainDelayRecyclerViewAdapter
        recyclerViewLayoutManager = train_delay_recyclerview.layoutManager as RecyclerView.LayoutManager

        //API叩く
        swipe_layout.isEnabled = false
        getTrainDelayAPI()
        //検索
        setSearch()

    }

    fun getTrainDelayAPI() {
        //読み込み中！
        isLoading = true
        swipe_layout.setRefreshing(true)
        //作成
        val request = Request.Builder()
            .url(trainDelayAPILink)
            .get()
            .build()

        //GETリクエスト
        val client = OkHttpClient()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                errorToast("")
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val jsonArray = JSONArray(response.body?.string())
                    //ぱーす
                    for (count in 0..jsonArray.length() - 1) {
                        val jsonObject = jsonArray.getJSONObject(count)
                        val name = jsonObject.getString("name")
                        val company = jsonObject.getString("company")
                        //配列
                        val item = arrayListOf<String>()
                        item.add("")
                        item.add(name)
                        item.add(company)
                        //反映
                        recyclerViewList.add(item)
                        trainDelayRecyclerViewAdapter.notifyItemInserted(count)
                    }
                    isLoading = false
                    swipe_layout.setRefreshing(false)
                } else {
                    errorToast(response.code.toString())
                }
            }

        })
    }

    fun errorToast(message: String) {
        activity?.runOnUiThread {
            Toast.makeText(context, "問題が発生しました。\n$message", Toast.LENGTH_SHORT).show()
        }
    }


    fun setSearch() {
        var searchList = arrayListOf<ArrayList<*>>()
            textinput_edit_layout.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    if (!isLoading){
                        if (p0?.length ?: 0 > 0) {
                            searchList.clear()
                            //配列消す
                            for (item in recyclerViewList) {
                                if ((item as ArrayList<String>).get(1).contains(p0.toString())) {
                                    searchList.add(item)
                                }
                            }
                            //検索内容をRecyclerViewへ
                            trainDelayRecyclerViewAdapter = TrainDelayRecyclerViewAdapter(searchList)
                            train_delay_recyclerview.adapter = trainDelayRecyclerViewAdapter
                            trainDelayRecyclerViewAdapter.notifyDataSetChanged()
                        } else {
                            trainDelayRecyclerViewAdapter = TrainDelayRecyclerViewAdapter(recyclerViewList)
                            train_delay_recyclerview.adapter = trainDelayRecyclerViewAdapter
                            trainDelayRecyclerViewAdapter.notifyDataSetChanged()
                        }
                    }
                }
            })
    }

}