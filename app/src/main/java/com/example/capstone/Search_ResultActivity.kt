package com.example.capstone

import android.app.ActionBar
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.preference.PreferenceManager
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.text.style.RelativeSizeSpan
import android.text.style.StyleSpan
import android.view.Gravity
import android.widget.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import java.lang.Exception
import java.lang.Math.abs
import java.lang.Math.round
import java.net.*
import kotlin.math.roundToInt
import org.json.JSONException

import org.json.JSONArray


class Search_ResultActivity : AppCompatActivity() {

    //맨위 상단 텍스트
    private lateinit var test_text : TextView
    //맨위 이전버튼
    private lateinit var button1 : Button
    //결과없음 출력용
    private lateinit var no_result : TextView

    private var Detail_bitmap : MutableList<Bitmap> = mutableListOf<Bitmap>()

    //그림을 저장할 공간
    private lateinit var bitmap: Bitmap

    //동적할당부분 코드
    //스크롤레이아웃
    private var scrolllayout : LinearLayout? = null
    //동적 배열 (텍스트 및 이미지 및 레이아웃)
    private var dynamic_text : MutableList<TextView> = mutableListOf<TextView>()
    private var dynamic_image : MutableList<ImageView> = mutableListOf<ImageView>()
    private var dynamic_layout : MutableList<LinearLayout> = mutableListOf<LinearLayout>()

    private var dynamic_image_background : MutableList<LinearLayout> = mutableListOf<LinearLayout>()
    private var dynamic_favorite_button : MutableList<Button> = mutableListOf<Button>()


    //얼마나 동적으로 늘릴지 정하는 코드
    private var getcount : Int = 0


    //평균정보 받아올 변수
    private lateinit var avg_count : String
    private lateinit var avg_price : String
    private lateinit var avg_distance : String

    private var avgpriceINT : Int = 0
    private var avgdistanceINT : Int = 0

    private var bottomNavigationView : BottomNavigationView? = null

    private lateinit var netip : String

    // ArrayList -> Json으로 변환
    private var box = ArrayList<Any>()
    private val SETTINGS_PLAYER_JSON = "테스트"

    private var jsonArr = JSONArray()
    private var jsonObj = JSONObject()

    override fun onCreate(savedInstanceState: Bundle?) {

        title = "검색결과"
        super.onCreate(savedInstanceState)
        setContentView(R.layout.search_result)

        //JSON받기
        box = getStringArrayPref(applicationContext, SETTINGS_PLAYER_JSON);

        //아이피주소받아오기
        netip = getString(R.string.net_ip)

        //테스트 출력용
        test_text = findViewById<TextView>(R.id.history_text)
        //결과없음 출력용
        no_result = findViewById<TextView>(R.id.testb)
        no_result.text = ""

        //이전버튼
        button1 = findViewById<Button>(R.id.history_button)
        button1.setOnClickListener {
            //var nextIntent = Intent(this,SearachActivity::class.java)
            //startActivity(nextIntent)
            finish()
        }

        //쓰레드 할 때 사용 변수
        var manu_out = ""
        var model_out = ""
        var model_detail_out = ""

        //테스트용 값 전달받는부분
        try{
            var tmp_settext = ""

            var tmp_manufacturer = intent.getStringExtra("manufacturer")
            var tmp_model = intent.getStringExtra("model")
            var tmp_model_detail = intent.getStringExtra("model_detail")

            tmp_settext = tmp_settext + "제조사 : " + tmp_manufacturer.toString()
            tmp_settext = tmp_settext + " / 모델명 : " + tmp_model.toString()
            tmp_settext = tmp_settext + " / 모델명_자세히 : " + tmp_model_detail.toString()

            test_text.text = tmp_settext

            //변수전달
            manu_out = tmp_manufacturer.toString()
            model_out = tmp_model.toString()
            model_detail_out = tmp_model_detail.toString()

        } catch (e : Exception){

        }

        /* 평균 정보 받아오는 부분 */

        var avg_text1 = findViewById<TextView>(R.id.avgtext1)
        var avg_text2 = findViewById<TextView>(R.id.avgtext2)
        var avg_text3 = findViewById<TextView>(R.id.avgtext3)

        avg_text2.text = "<평균 정보>"
        avg_text2.setTypeface(null,Typeface.BOLD)

        if(model_detail_out == "NULL"){
            if(model_out == "NULL"){
                avg_text1.text = manu_out
            }
            else{
                var tmp = manu_out + "_" + model_out
                avg_text1.text = tmp
            }
        }
        else{
            var tmp = manu_out + "_" + model_detail_out
            avg_text1.text = tmp
        }
        avg_text1.setTypeface(null,Typeface.BOLD)

        /* ------------------------------------------------------------ */

        //쓰레드를 하고 마칠 때 까지 기다린다.
        var thread = NetworkThread()
        thread.manu_net = manu_out
        thread.model_net = model_out
        thread.model_detail_net = model_detail_out

        thread.start()
        thread.join(5000)

        /* 평균 정보 받아오는 부분 */
        var avg_use1 = findViewById<TextView>(R.id.avg_use1)
        var avg_use2 = findViewById<TextView>(R.id.avg_use2)

        if (avg_count == "0"){
            avgpriceINT = 0
            avgdistanceINT = 0
        }
        else{
            avgpriceINT = avg_price.toDouble().roundToInt()
            avgdistanceINT = avg_distance.toDouble().roundToInt()
        }

        avg_use1.text = avgpriceINT.toString() + "원"
        avg_use2.text = avgdistanceINT.toString() + "KM"
        avg_use1.setTextColor(Color.parseColor("#4374D9"))
        avg_use2.setTextColor(Color.parseColor("#4374D9"))

        avg_text3.text = "검색 결과 총 " + avg_count + "대 존재합니다."
        avg_text3.setTextColor(Color.parseColor("#6B9900"))

        /* ------------------------------------------------------------ */

        //찜목록 체크 불러오기
        var caridArray : ArrayList<Any> = getStringArrayPref(applicationContext, "favorite")

        //동적할당 부분
        //스크롤 뷰에 추가한다.
        scrolllayout = findViewById(R.id.history_scroll) as LinearLayout

        for (i in 0 until getcount)
        {
            //레이아웃을 먼저 추가
            dynamic_layout.add(LinearLayout(this))

            //그림 3 : 글씨 3
            //사이즈 설정
            dynamic_layout[i].weightSum = 6f

            //이미지 추가
            dynamic_image_background.add(LinearLayout(this))
            dynamic_image_background[i].weightSum = 3f
            var ob = BitmapDrawable(Detail_bitmap[i])
            dynamic_image_background[i].background = ob

            //이미지레이아웃
            var tmplinear = LinearLayout.LayoutParams(0, ActionBar.LayoutParams.WRAP_CONTENT)
            tmplinear.weight = 3f
            tmplinear.height = 400
            tmplinear.gravity = Gravity.CENTER
            dynamic_image_background[i].layoutParams = tmplinear

            //버튼 추가
            dynamic_favorite_button.add(Button(this))
            var tmpbutton = LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT)
            tmpbutton.width = 100
            tmpbutton.height = 100
            tmpbutton.gravity = Gravity.RIGHT

            dynamic_favorite_button[i].layoutParams = tmpbutton

            //만약에 찜목록에 있다면 채워진하트, 아니면 빈하트
            var is_favorite : Boolean

            is_favorite = caridArray.contains(car_id[i])

            if (is_favorite){
                dynamic_favorite_button[i].background = getDrawable(R.drawable.heart2)
            }
            else{
                dynamic_favorite_button[i].background = getDrawable(R.drawable.heart1)
            }
            //dynamic_favorite_button[i].text = "버튼"
            dynamic_image_background[i]!!.addView(dynamic_favorite_button[i])

            dynamic_layout[i]!!.addView(dynamic_image_background[i])

            dynamic_favorite_button[i].setOnClickListener {

                var caridArray : ArrayList<Any> = getStringArrayPref(applicationContext, "favorite")

                if(caridArray.contains(car_id[i])){
                    Toast.makeText(applicationContext,"찜 목록에서 제거했습니다.",Toast.LENGTH_SHORT).show()
                    dynamic_favorite_button[i].background = getDrawable(R.drawable.heart1)
                    //찜목록에 제거
                    caridArray.remove(car_id[i])
                    setStringArrayPref(applicationContext, "favorite", caridArray)
                }
                else{
                    Toast.makeText(applicationContext,"찜 목록에 추가했습니다.",Toast.LENGTH_SHORT).show()
                    dynamic_favorite_button[i].background = getDrawable(R.drawable.heart2)
                    //찜목록에 추가
                    caridArray.add(0, car_id[i])
                    setStringArrayPref(applicationContext, "favorite", caridArray)
                }
            }

//            dynamic_image.add(ImageView(this))
//            dynamic_image[i].setImageBitmap(Detail_bitmap[i])
//            var tmpimage = LinearLayout.LayoutParams(0, ActionBar.LayoutParams.WRAP_CONTENT)
//            tmpimage.weight = 3f
//            tmpimage.height = 500
//            //tmpimage.gravity = Gravity.LEFT
//
//            dynamic_image[i].layoutParams = tmpimage
//            //scrolllayout!!.addView(dynamic_image[i])
//            dynamic_layout[i]!!.addView(dynamic_image[i])

            //텍스트 추가
            dynamic_text.add(TextView(this))

            //텍스트를 원하는대로 편집하는 부분
            /* 평균 가격 */
            var avg_distanceINT = avg_distance.toFloat().toInt()
            var avg_priceINT = avg_price.toFloat().toInt()

            var disgap = (distance[i].toInt() - avg_distanceINT)
            var prigap = (price[i].toInt() - avg_priceINT)

            var disper = 0f
            disper = round(((disgap / (avg_distanceINT).toFloat()) * 100) * 100)/100.toFloat()
            var priper = 0f
            priper = round(((prigap / (avg_priceINT).toFloat()) * 100) * 100)/100.toFloat()

            var tmptext = ""
            tmptext = tmptext + "■" + car_title[i] + "\n"
            tmptext = tmptext + "■" + "연식 :" + caryear[i] + "\n"
            tmptext = tmptext + "■" + "KM수 :" + distance[i] + "KM\n"


            var setcolortext1 = distance[i]
            var setcolortext1add = ""

            var setcolortext2 = price[i]
            var setcolortext2add = ""

            if(avg_distanceINT < distance[i].toInt()){
                setcolortext1add = "(" + disgap + "KM)(" + abs(disper) + "% 높음)\n"
                tmptext += setcolortext1add

            }
            else{
                setcolortext1add = "(" + disgap + "KM)(" + abs(disper) + "% 낮음)\n"
                tmptext += setcolortext1add
            }
            tmptext = tmptext + "■" + "가격 :" + price[i] + "원\n"
            if(avg_priceINT < price[i].toInt()){
                setcolortext2add = "(" + prigap + "원)(" + abs(priper) + "% 높음)\n"
                tmptext += setcolortext2add
            }
            else{
                setcolortext2add = "(" + prigap + "원)(" + abs(priper) + "% 낮음)\n"
                tmptext += setcolortext2add
            }

            var tx1 = textcolor()
            tx1.changetext = SpannableString(tmptext)
            tx1.keyword = setcolortext1
            if(avg_distanceINT < distance[i].toInt()){
                //빨강
                tx1.color = "#F15F5F"
                tx1.size = 1.3f
            }
            else{
                //초록
                tx1.color = "#47C83E"
                tx1.size = 1.3f
            }
            var tmpSS : SpannableString
            tmpSS = tx1.textcolorchange()

            tx1.changetext = SpannableString(tmpSS)
            tx1.keyword = setcolortext1add
            if(avg_distanceINT < distance[i].toInt()){
                //빨강
                tx1.color = "#F15F5F"
                tx1.size = 1.2f
            }
            else{
                //초록
                tx1.color = "#47C83E"
                tx1.size = 1.2f
            }
            tmpSS = tx1.textcolorchange()

            tx1.changetext = SpannableString(tmpSS)
            tx1.keyword = setcolortext2
            if(avg_priceINT < price[i].toInt()){
                //빨강
                tx1.color = "#F15F5F"
                tx1.size = 1.3f
            }
            else{
                //초록
                tx1.color = "#47C83E"
                tx1.size = 1.3f
            }
            tmpSS = tx1.textcolorchange()

            tx1.changetext = SpannableString(tmpSS)
            tx1.keyword = setcolortext2add
            if(avg_priceINT < price[i].toInt()){
                //빨강
                tx1.color = "#F15F5F"
                tx1.size = 1.1f
            }
            else{
                //초록
                tx1.color = "#47C83E"
                tx1.size = 1.1f
            }
            tmpSS = tx1.textcolorchange()

            dynamic_text[i].setText(tmpSS)

            /* -------------------------------------------- */

            //볼드체 처리
            //dynamic_text[i].setTypeface(null, Typeface.BOLD)

            var tmplayout = LinearLayout.LayoutParams(0, ActionBar.LayoutParams.WRAP_CONTENT)
            tmplayout.weight = 3f
            tmplayout.gravity = Gravity.CENTER
            dynamic_text[i].layoutParams = tmplayout
            //scrolllayout!!.addView(dynamic_text[i])
            dynamic_layout[i]!!.addView(dynamic_text[i])

            scrolllayout!!.addView(dynamic_layout[i])

            //표 만드는 부분
            dynamic_layout[i].setBackgroundResource(R.drawable.border_bottom)
            //클릭 부분
            dynamic_layout[i].setOnClickListener {

                var caridArray : ArrayList<Any> = getStringArrayPref(applicationContext, "history")

                if (caridArray.contains(car_id[i])) {
                    caridArray.remove(car_id[i])
                }
                if (caridArray.size >= 20) {
                    caridArray.removeAt(caridArray.size - 1)
                }
                caridArray.add(0, car_id[i])
                setStringArrayPref(applicationContext, "history", caridArray)

                var nextIntent = Intent(this,Search_Result_InfomationActivity::class.java)
                nextIntent.putExtra("url_address", url_address[i]) //0
                nextIntent.putExtra("sitetype", sitetype[i]) //1
                nextIntent.putExtra("car_title", car_title[i]) //2
                nextIntent.putExtra("carnumber", carnumber[i]) //3
                nextIntent.putExtra("cartype", cartype[i]) //4
                nextIntent.putExtra("manufacturer", manufacturer[i]) //5
                nextIntent.putExtra("model", model[i]) //6
                nextIntent.putExtra("model_detail", model_detail[i]) //7
                nextIntent.putExtra("price", price[i]) //8
                nextIntent.putExtra("distance", distance[i]) //9
                nextIntent.putExtra("caryear", caryear[i]) //10
                nextIntent.putExtra("carcolor", carcolor[i]) //11
                nextIntent.putExtra("carfuel", carfuel[i]) //12
                nextIntent.putExtra("imglink", imglink[i]) //13
                nextIntent.putExtra("car_id",car_id[i]) //14
                startActivity(nextIntent)
            }
        }
    }

    inner class textcolor{
        public lateinit var changetext : SpannableString
        public var keyword = ""
        public var color = ""
        public var size = 0f
        public fun textcolorchange(): SpannableString {
            var colortext = SpannableString(changetext)
            var word = keyword
            var colortext_start = changetext.indexOf(word)
            var colortext_end = colortext_start + word.length
            /*
                setSpan (속성, 시작 위치, 끝 위치, 플래그)
                ForegroundColorSpan : 글자 색상 지정. (BackgroundColorSpan : 배경색 지정)
                StyleSpan : 글자의 스타일 지정 (BOLD, ITALIC 등)
                RelativeSizeSpan : 글자의 상대적 크기 지정 (1.3f는 1.3배)
            */
            colortext.setSpan(ForegroundColorSpan(Color.parseColor(color)), colortext_start, colortext_end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            colortext.setSpan(StyleSpan(Typeface.BOLD), colortext_start, colortext_end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            colortext.setSpan(RelativeSizeSpan(size), colortext_start, colortext_end, SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE);

            return colortext
        }
    }

    private fun setStringArrayPref(context: Context, key: String, values: ArrayList<Any>) {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val editor = prefs.edit()
        val a = JSONArray()
        for (i in 0 until values.size) {
            a.put(values[i])
        }
        if (!values.isEmpty()) {
            editor.putString(key, a.toString())
        } else {
            editor.putString(key, null)
        }
        editor.apply()
    }

    private fun getStringArrayPref(context: Context, key: String) : ArrayList<Any> {
        val prefs = PreferenceManager.getDefaultSharedPreferences(context)
        val json = prefs.getString(key, null)
        val urls = ArrayList<Any>()
        if (json != null) {
            try {
                val a = JSONArray(json)
                for (i in 0 until a.length()) {
                    val url = a.optString(i)
                    urls.add(url)
                }
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return urls
    }


    //그림 가져오기
    inner class uThread : Thread() {
        var str = ""
        override fun run() {
            val tmp = "http://" + netip + "/" + str
            Log.d("주소",tmp)
            try {
                val url = URL(tmp)
                var conn: HttpURLConnection? = null
                conn = url.openConnection() as HttpURLConnection

                //접속오류시 연결안함
                conn.connectTimeout = 5000
                conn!!.connect()
                try {
                    //연결 성공, 이미지인경우
                    val aaa = conn.inputStream //inputStream 값 가져오기
                    bitmap = BitmapFactory.decodeStream(aaa) // Bitmap으로 반환
                } catch (e: Exception) {
                    //연결은 성공했지만, 이미지가 아닌경우입니다.
                }
            } catch (SocketTimeoutException: Exception) {
            }
        }
    }

    // 자동차 ID
    private var car_id : MutableList<String> = mutableListOf<String>()
    // URL 주소
    private var url_address : MutableList<String> = mutableListOf<String>()
    // 사이트 타입
    private var sitetype : MutableList<String> = mutableListOf<String>()
    // title = 자동차 전체 이름 ex(현대 LF 쏘나타 LPI 프리미엄)
    private var car_title : MutableList<String> = mutableListOf<String>()
    // 자동차 번호
    private var carnumber : MutableList<String> = mutableListOf<String>()
    // 자동차 타입 ex(중형차)
    private var cartype : MutableList<String> = mutableListOf<String>()
    // 제조사
    private var manufacturer : MutableList<String> = mutableListOf<String>()
    // 모델명 ex(쏘나타)
    private var model : MutableList<String> = mutableListOf<String>()
    // 모델명_자세히 ex(LF 쏘나타)
    private var model_detail : MutableList<String> = mutableListOf<String>()
    // 가격
    private var price : MutableList<String> = mutableListOf<String>()
    // 주행거리
    private var distance : MutableList<String> = mutableListOf<String>()
    // 연식
    private var caryear : MutableList<String> = mutableListOf<String>()
    // 색깔
    private var carcolor : MutableList<String> = mutableListOf<String>()
    // 연료타입
    private var carfuel : MutableList<String> = mutableListOf<String>()
    // 이미지링크
    private var imglink : MutableList<String> = mutableListOf<String>()

    //서버통신
    inner class NetworkThread: Thread() {

        var manu_net = ""
        var model_net = ""
        var model_detail_net = ""

        override fun run() {
            // 접속할 페이지 주소: Site

            var site = "http://"+ netip +"/car_search.php?manufacturer="+manu_net+"&model="+model_net+"&model_detail="+model_detail_net
            Log.d("테스트ppp",site)

            var url = URL(site)
            var conn = url.openConnection()
            var input = conn.getInputStream()
            var isr = InputStreamReader(input)
            // br: 라인 단위로 데이터를 읽어오기 위해서 만듦
            var br = BufferedReader(isr)

            // Json 문서는 일단 문자열로 데이터를 모두 읽어온 후, Json에 관련된 객체를 만들어서 데이터를 가져옴
            var str: String? = null
            var buf = StringBuffer()

            do {
                str = br.readLine()

                if (str != null) {
                    buf.append(str)
                }

            } while (str != null)
            //Log.d("태그", "3")
            // 전체가 객체로 묶여있기 때문에 객체형태로 가져옴
            var root = JSONObject(buf.toString())


            /* 차량 개수 먼저 받아오기 */

            var result2 = root.getJSONArray("result2")
            var obj2 = result2.getJSONObject(0)
            avg_count = obj2.getString("count")
            avg_price = obj2.getString("priceAVG")
            avg_distance = obj2.getString("distAVG")

            /* ---------------------------------------------- */

            if (avg_count.toInt() != 0) {
                // 객체 안에 있는 stores라는 이름의 리스트를 가져옴
                var result = root.getJSONArray("result")

                //받아오는 데이터의 개수
                var lenofresult = result.length()

                //너무 많으면 속도저하가 있기 때문에, 최초에 15개만 통신한다.
                if (lenofresult > 15){
                    lenofresult = 15
                }

                for (i in 0 until lenofresult){
                    var obj2 = result.getJSONObject(i)
                    car_id.add(obj2.getString("carid"))
                    url_address.add(obj2.getString("url"))
                    sitetype.add(obj2.getString("site"))
                    car_title.add(obj2.getString("title"))
                    carnumber.add(obj2.getString("carnumber"))
                    cartype.add(obj2.getString("cartype"))
                    manufacturer.add(obj2.getString("manufacturer"))
                    model.add(obj2.getString("model"))
                    model_detail.add(obj2.getString("model_detail"))
                    price.add(obj2.getString("price"))
                    distance.add(obj2.getString("distance"))
                    caryear.add(obj2.getString("caryear"))
                    carcolor.add(obj2.getString("carcolor"))
                    carfuel.add(obj2.getString("carfuel"))
                    imglink.add(obj2.getString("imglink"))
                }

                for (i in 0 until lenofresult){
                    //이미지 처리 부분
                    var thread1 = uThread()
                    thread1.str = imglink[i]
                    thread1.start()
                    thread1.join()
                    Detail_bitmap.add(bitmap)
                }
                // 밖에 Json길이를 전달해줘야함
                getcount = lenofresult
            }
            else{
                getcount = 0
                no_result.setText("결과 없음")
                no_result.gravity = Gravity.CENTER
                no_result.setTypeface(null, Typeface.BOLD)
            }
//            try{
////                do {
////                    str = br.readLine()
////
////                    if (str != null) {
////                        buf.append(str)
////                    }
////
////                } while (str != null)
////                //Log.d("태그", "3")
////                // 전체가 객체로 묶여있기 때문에 객체형태로 가져옴
////                var root = JSONObject(buf.toString())
////
////    //            var userName: String = root.getString("site")
////                //var userPhoneNumber: String = root.getString("userPhoneNumber")
////
////                // 객체 안에 있는 stores라는 이름의 리스트를 가져옴
////                var result = root.getJSONArray("result")
////
////                //받아오는 데이터의 개수
////                var lenofresult = result.length()
////
////                //너무 많으면 속도저하가 있기 때문에, 최초에 15개만 통신한다.
////                if (lenofresult > 15){
////                    lenofresult = 15
////                }
////
////                for (i in 0 until lenofresult){
////                    var obj2 = result.getJSONObject(i)
////                    car_id.add(obj2.getString("carid"))
////                    url_address.add(obj2.getString("url"))
////                    sitetype.add(obj2.getString("site"))
////                    car_title.add(obj2.getString("title"))
////                    carnumber.add(obj2.getString("carnumber"))
////                    cartype.add(obj2.getString("cartype"))
////                    manufacturer.add(obj2.getString("manufacturer"))
////                    model.add(obj2.getString("model"))
////                    model_detail.add(obj2.getString("model_detail"))
////                    price.add(obj2.getString("price"))
////                    distance.add(obj2.getString("distance"))
////                    caryear.add(obj2.getString("caryear"))
////                    carcolor.add(obj2.getString("carcolor"))
////                    carfuel.add(obj2.getString("carfuel"))
////                    imglink.add(obj2.getString("imglink"))
////                }
////
////                for (i in 0 until lenofresult){
////                    //이미지 처리 부분
////                    var thread1 = uThread()
////                    thread1.str = imglink[i]
////                    thread1.start()
////                    thread1.join()
////                    Detail_bitmap.add(bitmap)
////                }
////
////                // 밖에 Json길이를 전달해줘야함
////                getcount = lenofresult
////
////                var result2 = root.getJSONArray("result2")
////                var lenofresult2 = result2.length()
////
////                for (i in 0 until lenofresult2){
////                    var obj2 = result2.getJSONObject(i)
////                    avg_count = obj2.getString("count")
////                    avg_price = obj2.getString("priceAVG")
////                    avg_distance = obj2.getString("distAVG")
////                }
//
//
////                if (lenofresult >= 4){
////                    lenofresult = 4
////                }
////                for (i in 0 until lenofresult) {
////                    // 화면에 출력
////                    runOnUiThread {
////    //                    var obj2 = result.getJSONObject(i)
////    //                    url_address.add(obj2.getString("url"))
////    //                    imglink.add(obj2.getString("imglink"))
////                        //var obj2 = result.getJSONObject(i)
////
////                        //url_address.add(obj2.getString("url"))
////                        //imglink.add(obj2.getString("imglink"))
////    //                    Log.d("i값",i.toString())
////                        var tmptext = ""
////                        tmptext = tmptext + car_title[i] + "\n"
////                        tmptext = tmptext + "연식 : " + caryear[i] + "\n"
////                        tmptext = tmptext + "KM수 : " + distance[i] + "KM" + "\n"
////                        tmptext = tmptext + "연료 : " + carfuel[i] + "\n"
////                        tmptext = tmptext + "가격 : " + price[i] + "원" + "\n"
////
////    //                    tmptext = tmptext + "사이트위치: ${sitetype}\n"
////    //                    tmptext = tmptext + "자동차종류: ${cartype}\n"
////    //                    tmptext = tmptext + "가격: ${price} 원\n"
////                        search_resultactivitytextbox[i].setText(tmptext)
////    //                    text2.append("url: ${urlstr}\n")
////    //                    text2.append("site: ${sitetype}\n")
////    //                    text2.append("cartype: ${cartype}\n")
////    //                    text2.append("price: ${price}\n")
////    //                    text2.append("imglink: ${imglink}\n")
////
////                        //이미지 처리 부분
////                        var thread1 = uThread()
////                        thread1.str = "http://"+ netip +"/" + imglink[i]
////                        thread1.start()
////                        thread1.join()
////                        search_resultactivityimagebox[i].setImageBitmap(bitmap)
////                        search_resultactivityURL.add(url_address[i]);
////                    }
////                }
//            }catch(e : Exception){
//                no_result.setText("결과 없음")
//                no_result.gravity = Gravity.CENTER
//                no_result.setTypeface(null, Typeface.BOLD)
//            }
        }
    }
}

