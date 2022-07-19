package com.example.poker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.poker.R

class RankActivity : AppCompatActivity() {
    private val items = mutableListOf<RankContents>()
    lateinit var RVAdapter: RVAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_rank)
        initRecyclerView()
    }

    private fun initRecyclerView() {
        RVAdapter = RVAdapter(this,items)
        items.add(
            RankContents("CHALLENGER : 대통령", "over 100000","https://w.namu.la/s/0c0f87ddf398b364b7b42def9f082db931d139dc905e72a5e7bfdb01e098501df530fbb2e6aa0aa6b9c717f6c9a08f668f036a8b2ad7058eee8c25cddc7f377566b1f19ed5fe31f5629fb1723ae78ede2f69a3c77aaac349ad293b2541200ca58ad464acb30ec8b73e82052fa156dcb1")
        )
        items.add(
            RankContents("GRANDMASTER : 재벌", "over 50000","https://w.namu.la/s/1c84799bd99e36980c9212001d399785e0c6548c316b21ded46164e1edcc17543f1b9e5e2735365412b0de8d7032accc9665c6f2179620c010f34fc637e33d6f37e7938a0408653c2ddfd52934822069325e453f4491efd996d6410538a0939a")
        )
        items.add(
            RankContents("MASTER :  슈퍼스타","over 20000", "https://w.namu.la/s/d8db6e871b8e77712495cacc1e775c059ed4e861bad0415543b99264f9f9be10d449e6fecdc26da44b4b1ded2fe0f88b16f1f3481f87e3b044654b693ce53b828720746e4b0e087b19d346dc784714f72c5bf47f3e8caad614e34ad66dfff770")
        )
        items.add(
            RankContents("DIAMOND : 스타", "over 10000","https://w.namu.la/s/5b5eda7648632a25ad3e8d43d1cae8be09b7f122fc5ecca894cd62f5a479714c074154d5d71132393862844f0400bd916a8464b8b1d413a9df845955b11e53490f86402b9d1b8a39bce7cfee890c1f1411dde820f151319a29dc03be35385622f672be6beeda6b282607995d01feb0a3")
        )
        items.add(
            RankContents("PLATINUM : 총장", "over 7000","https://cdn.times.kaist.ac.kr/news/photo/202201/20674_20307_5949.jpg")
        )
        items.add(
            RankContents("GOLD : 교수", "over 5000","https://cs.kaist.ac.kr/upload_files/bbs/news/201509/55e94f342bf3f_view.jpg")
        )
        items.add(
            RankContents("SILVER : 새내기", "over 3000","https://post-phinf.pstatic.net/MjAyMTA1MjRfMzEg/MDAxNjIxODI0NzI5MjAz.xgweJ2A1OQQo2MaxWVuTlwvt4oTLRI_OaNByNI0GSqwg._okYII5dJOhfwNuaBsY2iuEaC9zunIJHOg855qunyZ4g.JPEG/어린이집_유치원_병아리_캐릭터_그리기_%2814%29.jpg?type=w1200")
        )
        items.add(
            RankContents("BRONZE : 헌내기", "over 2000","https://mblogthumb-phinf.pstatic.net/20140117_255/korea_gov_1389937807953NoKJn_JPEG/1_KCN%C4%AE%B7%B3.jpg?type=w2")
        )
        items.add(
            RankContents("IRON : 대학원생", "over 0","https://pbs.twimg.com/profile_images/1259491429933477888/l47ZQEq1_400x400.jpg")
        )
        val recyclerView = findViewById<RecyclerView>(R.id.rv)
        recyclerView?.adapter = RVAdapter
        recyclerView?.layoutManager = GridLayoutManager(this, 1)
        RVAdapter.notifyDataSetChanged()
    }
}