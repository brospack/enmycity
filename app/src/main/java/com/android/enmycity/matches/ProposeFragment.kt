package com.android.enmycity.matches

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.LinearLayout.VERTICAL
import com.android.enmycity.R
import com.android.enmycity.data.UserSharedPreferences
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_propose.propose_progressBar
import kotlinx.android.synthetic.main.fragment_propose.propose_proposes_recyclerView
import org.jetbrains.anko.toast

class ProposeFragment : Fragment(), ProposeView {
  private val presenter: ProposePresenter by lazy { ProposePresenter(UserSharedPreferences(context!!), FirebaseFirestore.getInstance()) }
  private lateinit var proposesAdapter: ProposesAdapter

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
      inflater.inflate(R.layout.fragment_propose, container, false)

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    initRecyclerView()
    presenter.setView(this)
    presenter.onViewReady()
  }

  private fun initRecyclerView() {
    proposesAdapter = ProposesAdapter(mutableListOf(), context!!)
    propose_proposes_recyclerView.apply {
      setHasFixedSize(true)
      layoutManager = LinearLayoutManager(context, VERTICAL, false)
      adapter = proposesAdapter
    }
  }

  override fun showPropose(propose: ProposeViewModel): Unit = proposesAdapter.let {
    it.addPropose(propose)
    it.notifyDataSetChanged()
  }

  override fun showEmptyData() {
    context?.toast("GOLA")
  }

  override fun showLoading() {
    propose_progressBar.visibility = VISIBLE
  }

  override fun hideLoading() {
    propose_progressBar.visibility = GONE
  }
}
