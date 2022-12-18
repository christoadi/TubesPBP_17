package com.example.ugd1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ugd1.api.KritikSaranApi
import com.example.ugd1.model.KritikSaranModel
import com.google.gson.Gson
import org.json.JSONObject
import java.nio.charset.StandardCharsets

class EditKritikSaran : AppCompatActivity() {
    private var etId: EditText? = null
    private var etNama: EditText? = null
    private var etKritik: EditText? = null
    private var edSaran: EditText? = null
    private var edNomorTelepon: EditText? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_kritik_saran)

        //Pendeklarasian request queue
        queue = Volley.newRequestQueue(this)
        etId = findViewById(R.id.et_id)
        etNama = findViewById(R.id.et_nama)
        etKritik = findViewById(R.id.et_kritik)
        edSaran = findViewById(R.id.et_saran)
        edNomorTelepon = findViewById(R.id.et_nomorTelepon)
        layoutLoading = findViewById(R.id.layout_loading)
//
//        setExposedDropDownMenu()

        val btnCancel = findViewById<Button>(R.id.btn_cancel)
        btnCancel.setOnClickListener { finish() }
        val btnSave = findViewById<Button>(R.id.btn_save)
        val tvTitle = findViewById<TextView>(R.id.tv_title)
        val id = intent.getIntExtra("id", -1)
        if (id == -1) {
            btnSave.setOnClickListener {
                val nama = etNama!!.text.toString()
                val kritik = etKritik!!.text.toString()
                val saran = edSaran!!.text.toString()
                val nomorTelepon = edNomorTelepon!!.text.toString()

                tvTitle.setText("Tambah Kritik Saran")

                createKritikSaran()
//                createPdf(namaPemesan, namaBarang, jumlah, tanggalAmbil)
            }


        } else {
            tvTitle.setText("Edit Kritik Saran")
            getKritikSaranById(id)

            btnSave.setOnClickListener { updateKritikSaran(id) }
        }

    }

//    fun setExposedDropDownMenu(){
//        val adapterFakultas: ArrayAdapter<String> = ArrayAdapter<String>(this,
//            R.layout.item_list, FAKULTAS_LIST)
//        edFakultas!!.setAdapter(adapterFakultas)
//
//        val adapterProdi: ArrayAdapter<String> = ArrayAdapter<String>(this,
//            R.layout.item_list, PRODI_LIST)
//        edProdi!!.setAdapter(adapterProdi)
//    }

    private fun getKritikSaranById(id: Int) {
        // Fungsi untuk menampilkan data pesanan berdasarkan id
        setLoading(true)
        val stringRequest: StringRequest =
            object : StringRequest(
                Method.GET,
                KritikSaranApi.GET_BY_ID_URL + id,
                Response.Listener { response ->
//                val gson = Gson()
//                val member = gson.fromJson(response, MemberGym::class.java)

                    var memberJo = JSONObject(response.toString())
                    val member = memberJo.getJSONObject("data")

                    etNama!!.setText(member.getString("nama"))
                    etKritik!!.setText(member.getString("kritik"))
                    edSaran!!.setText(member.getString("saran"))
                    edNomorTelepon!!.setText(member.getString("nomorTelepon"))

                    Toast.makeText(this@EditKritikSaran, "Data berhasil diambil", Toast.LENGTH_SHORT)
                        .show()
                    setLoading(false)
                },
                Response.ErrorListener { error ->
                    setLoading(false)
                    try {
                        val responseBody =
                            String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
                        Toast.makeText(
                            this@EditKritikSaran,
                            errors.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        Toast.makeText(this@EditKritikSaran, e.message, Toast.LENGTH_SHORT).show()
                    }
                }) {
                @Throws(AuthFailureError::class)
                override fun getHeaders(): MutableMap<String, String> {
                    val headers = HashMap<String, String>()
                    headers["Accept"] = "application/json"
                    return headers
                }
            }
        queue!!.add(stringRequest)
    }

    private fun createKritikSaran() {
        setLoading(true)

        if (etNama!!.text.toString().isEmpty()) {
            Toast.makeText(
                this@EditKritikSaran,
                "Nama tidak boleh kosong!",
                Toast.LENGTH_SHORT
            ).show()
        } else if (etKritik!!.text.toString().isEmpty()) {
            Toast.makeText(this@EditKritikSaran, "Kritik tidak boleh kosong!", Toast.LENGTH_SHORT)
                .show()
        } else if (edSaran!!.text.toString().isEmpty()) {
            Toast.makeText(this@EditKritikSaran, "Saran tidak boleh kosong!", Toast.LENGTH_SHORT)
                .show()
        } else if (edNomorTelepon!!.text.toString().isEmpty()) {
            Toast.makeText(this@EditKritikSaran, "Nomor Telepon tidak boleh kosong!", Toast.LENGTH_SHORT)
                .show()
        } else {

            val kritikSaran = KritikSaranModel(
                0,
                etNama!!.text.toString(),
                etKritik!!.text.toString(),
                edSaran!!.text.toString(),
                edNomorTelepon!!.text.toString()
            )
            val stringRequest: StringRequest =
                object :
                    StringRequest(Method.POST, KritikSaranApi.ADD_URL, Response.Listener { response ->
                        val gson = Gson()
                        val kritikSaran = gson.fromJson(response, KritikSaranModel::class.java)

                        if (kritikSaran != null)
                            Toast.makeText(
                                this@EditKritikSaran,
                                "Data Berhasil Ditambahkan",
                                Toast.LENGTH_SHORT
                            ).show()

                        val returnIntent = Intent()
                        setResult(RESULT_OK, returnIntent)
                        finish()

                        setLoading(false)
                    }, Response.ErrorListener { error ->
                        setLoading(false)
                        try {
                            val responseBody =
                                String(error.networkResponse.data, StandardCharsets.UTF_8)
                            val errors = JSONObject(responseBody)
                            Toast.makeText(
                                this@EditKritikSaran,
                                errors.getString("message"),
                                Toast.LENGTH_SHORT
                            ).show()
                        } catch (e: Exception) {
                            Toast.makeText(this@EditKritikSaran, e.message, Toast.LENGTH_SHORT).show()
                        }
                    }) {
                    @Throws(AuthFailureError::class)
                    override fun getHeaders(): MutableMap<String, String> {
                        val headers = HashMap<String, String>()
                        headers["Accept"] = "application/json"
                        return headers

                    }

                    @Throws(AuthFailureError::class)
                    override fun getBody(): ByteArray {
                        val gson = Gson()
                        val requestBody = gson.toJson(kritikSaran)
                        return requestBody.toByteArray(StandardCharsets.UTF_8)
                    }

                    override fun getBodyContentType(): String {
                        return "application/json"
                    }
                }
            // Menambahkan request ke request queue
            queue!!.add(stringRequest)
        }
        setLoading(false)
    }

    private fun updateKritikSaran(id: Int) {
        setLoading(true)

        val kritikSaran = KritikSaranModel(
            id,
            etNama!!.text.toString(),
            etKritik!!.text.toString(),
            edSaran!!.text.toString(),
            edNomorTelepon!!.text.toString()
        )

        val stringRequest: StringRequest = object :
            StringRequest(
                Method.PUT,
                KritikSaranApi.UPDATE_URL + id,
                Response.Listener { response ->
                    val gson = Gson()

                    val kritikSaran = gson.fromJson(response, KritikSaranModel::class.java)

                    if (kritikSaran != null)
                        Toast.makeText(
                            this@EditKritikSaran,
                            "Data Berhasil Diupdate",
                            Toast.LENGTH_SHORT
                        ).show()
                    val returnIntent = Intent()
                    setResult(RESULT_OK, returnIntent)
                    finish()

                    setLoading(false)
                },
                Response.ErrorListener { error ->
                    setLoading(false)
                    try {
                        val responseBody =
                            String(error.networkResponse.data, StandardCharsets.UTF_8)
                        val errors = JSONObject(responseBody)
                        Toast.makeText(
                            this@EditKritikSaran,
                            errors.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        Toast.makeText(this@EditKritikSaran, e.message, Toast.LENGTH_SHORT).show()
                    }
                }) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Accept"] = "application/json"
                return headers

            }

            @Throws(AuthFailureError::class)
            override fun getBody(): ByteArray {
                val gson = Gson()
                val requestBody = gson.toJson(kritikSaran)
                return requestBody.toByteArray(StandardCharsets.UTF_8)
            }

            override fun getBodyContentType(): String {
                return "application/json"
            }
        }
        queue!!.add(stringRequest)
    }

    //Fungsi ini digunakan untuk menampilkan layout Loading


//    @SuppressLint("ObsoleteSdkInt")
//    @RequiresApi(api = Build.VERSION_CODES.O)
//    @Throws(
//        FileNotFoundException::class
//    )
//    private fun createPdf(
//        namaPemesan: String,
//        namaBarang: String,
//        jumlah: String,
//        tanggalAmbil: String
//    ) {
//        //akses writing ke storage hp dalam mode download
//        val pdfPath =
//            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
//                .toString()
//        val file = File(pdfPath, "pdf_17.pdf")
//        FileOutputStream(file)
//
//        //inisialisasi pembuatan PDF
//        val writer = PdfWriter(file)
//        val pdfDocument = PdfDocument(writer)
//        val document = Document(pdfDocument)
//        pdfDocument.defaultPageSize = PageSize.A4
//        document.setMargins(5f, 5f, 5f, 5f)
//        @SuppressLint("useCompatLoadingForDrawables") val d = getDrawable(R.drawable.gym)
//
//        //penambahan gambar
//        val bitmap = (d as BitmapDrawable)!!.bitmap
//        val stream = ByteArrayOutputStream()
//        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
//        val bitmapData = stream.toByteArray()
//        val imageData = ImageDataFactory.create(bitmapData)
//        val image = Image(imageData)
//        val namapengguna = Paragraph("Identitas Pengguna").setBold().setFontSize(24f)
//            .setTextAlignment(TextAlignment.CENTER)
//        val group = Paragraph(
//            """
//                                    Berikut adalah
//                                    Nama Pengguna UAJY 2022/2023
//                                """.trimIndent()
//        ).setTextAlignment(TextAlignment.CENTER).setFontSize(12f)
//
//        //pembuatan table
//        val width = floatArrayOf(100f, 100f)
//        val table = Table(width)
//        //pengisian data ke dalam tabel
//        table.setHorizontalAlignment(HorizontalAlignment.CENTER)
//        table.addCell(Cell().add(Paragraph("Personal Trainer")))
//        table.addCell(Cell().add(Paragraph(namaPemesan)))
//        table.addCell(Cell().add(Paragraph("namaBarang")))
//        table.addCell(Cell().add(Paragraph(namaBarang)))
//        table.addCell(Cell().add(Paragraph("Tanggal")))
//        table.addCell(Cell().add(Paragraph(tanggal)))
//        table.addCell(Cell().add(Paragraph("jumlah")))
//        table.addCell(Cell().add(Paragraph(jumlah)))
//        val dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
//        table.addCell(Cell().add(Paragraph("Tanggal Buat PDF")))
//        table.addCell(Cell().add(Paragraph(LocalDate.now().format(dateTimeFormatter))))
//        val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss a")
//        table.addCell(Cell().add(Paragraph("Pukul Pembuatan")))
//        table.addCell(Cell().add(Paragraph(LocalTime.now().format(timeFormatter))))
//
//        //pembuatan QR CODE secara generate dengan bantuan IText7
//        val barcodeQRCode = BarcodeQRCode(
//            """
//            $namaPemesan
//            $namaBarang
//            $tanggal
//            $jumlah
//            ${LocalDate.now().format(dateTimeFormatter)}
//            ${LocalTime.now().format(timeFormatter)}
//        """.trimIndent()
//        )
//        val qrCodeObject = barcodeQRCode.createFormXObject(ColorConstants.BLACK, pdfDocument)
//        val qrCodeImage = Image(qrCodeObject).setWidth(80f).setHorizontalAlignment(
//            HorizontalAlignment.CENTER
//        )
//
//        document.add(image)
//        document.add(namapengguna)
//        document.add(group)
//        document.add(table)
//        document.add(qrCodeImage)
//
//        document.close()
//        Toast.makeText(this, "Pdf Created", Toast.LENGTH_SHORT).show()
//    }

    private fun setLoading(isLoading: Boolean) {
        if (isLoading) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
            )
            layoutLoading!!.visibility = View.VISIBLE
        } else {
            window.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
            layoutLoading!!.visibility = View.INVISIBLE
        }
    }
}