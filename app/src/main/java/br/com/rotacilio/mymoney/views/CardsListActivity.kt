package br.com.rotacilio.mymoney.views

import android.content.Intent
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import android.widget.Toast
import br.com.rotacilio.mymoney.R
import br.com.rotacilio.mymoney.adapters.CardsAdapter
import br.com.rotacilio.mymoney.commons.CallbackRequest
import br.com.rotacilio.mymoney.domain.Card
import br.com.rotacilio.mymoney.requests.CardsRequest
import kotlinx.android.synthetic.main.activity_cards_list.*

class CardsListActivity : AppCompatActivity(), CallbackRequest, View.OnClickListener {

    private var mCards: MutableList<Card>? = null
    private var mAdapter: CardsAdapter? = null

    private var onRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        loadData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cards_list)
        swipeRefreshCards.setOnRefreshListener(onRefreshListener)
        configureRecyclerView()
        fab.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        loadData()
    }

    private fun configureRecyclerView() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerCards.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(recyclerCards.context, layoutManager.orientation)
        recyclerCards.addItemDecoration(itemDecoration)
        mAdapter = CardsAdapter()
        recyclerCards.adapter = mAdapter
    }

    private fun loadData() {
        val request = CardsRequest()
        request.findAll(true, this, this)
    }

    override fun success(response: Any) {
        mCards = response as MutableList<Card>
        mAdapter?.mCards = mCards as MutableList<Card>
        if (swipeRefreshCards.isRefreshing) {
            swipeRefreshCards.isRefreshing = false
        }
    }

    override fun failure(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        if (swipeRefreshCards.isRefreshing) {
            swipeRefreshCards.isRefreshing = false
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fab -> startActivity(Intent(this, NewCardActivity::class.java))
        }
    }
}