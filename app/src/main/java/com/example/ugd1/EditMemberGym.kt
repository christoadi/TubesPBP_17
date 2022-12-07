package com.example.ugd1

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.view.WindowManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.android.volley.AuthFailureError
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.ugd1.api.MemberGymApi
import com.example.ugd1.model.MemberGymModel
import com.example.ugd1.room.*
import com.google.gson.Gson
import com.itextpdf.barcodes.BarcodeQRCode
import com.itextpdf.io.image.ImageDataFactory
import com.itextpdf.io.source.ByteArrayOutputStream
import com.itextpdf.kernel.colors.ColorConstants
import com.itextpdf.kernel.geom.PageSize
import com.itextpdf.kernel.pdf.PdfDocument
import com.itextpdf.kernel.pdf.PdfWriter
import com.itextpdf.layout.Document
import com.itextpdf.layout.element.Cell
import com.itextpdf.layout.element.Image
import com.itextpdf.layout.element.Paragraph
import com.itextpdf.layout.element.Table
import com.itextpdf.layout.property.HorizontalAlignment
import com.itextpdf.layout.property.TextAlignment
import kotlinx.android.synthetic.main.activity_edit_member_gym.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.nio.charset.StandardCharsets
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class EditMemberGym : AppCompatActivity() {

    private var etId: EditText? = null
    private var etPersonalTrainer: EditText? = null
    private var etMembership: EditText? = null
    private var edTanggal: EditText? = null
    private var edDurasi: EditText? = null
    private var layoutLoading: LinearLayout? = null
    private var queue: RequestQueue? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_member_gym)

        //Pendeklarasian request queue
        queue = Volley.newRequestQueue(this)
        etId = findViewById(R.id.et_id)
        etPersonalTrainer = findViewById(R.id.et_personalTrainer)
        etMembership = findViewById(R.id.et_membership)
        edTanggal = findViewById(R.id.et_tanggal)
        edDurasi = findViewById(R.id.et_durasi)
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
                val personalTrainer = etPersonalTrainer!!.text.toString()
                val membership = etMembership!!.text.toString()
                val tanggal = edTanggal!!.text.toString()
                val durasi = edDurasi!!.text.toString()

                tvTitle.setText("Tambah Member Gym")

                createMahasiswa()
                createPdf(personalTrainer, membership, tanggal, durasi)
            }


        } else {
            tvTitle.setText("Edit Member Gym")
            getMahasiswaById(id)

            btnSave.setOnClickListener { updateMahasiswa(id) }
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

    private fun getMahasiswaById(id: Int) {
        // Fungsi untuk menampilkan data mahasiswa berdasarkan id
        setLoading(true)
        val stringRequest: StringRequest =
            object : StringRequest(
                Method.GET,
                MemberGymApi.GET_BY_ID_URL + id,
                Response.Listener { response ->
//                val gson = Gson()
//                val member = gson.fromJson(response, MemberGym::class.java)

                    var memberJo = JSONObject(response.toString())
                    val member = memberJo.getJSONObject("data")

                    etPersonalTrainer!!.setText(member.getString("personalTrainer"))
                    etMembership!!.setText(member.getString("membership"))
                    edTanggal!!.setText(member.getString("tanggal"))
                    edDurasi!!.setText(member.getString("durasi"))

                    Toast.makeText(this@EditMemberGym, "Data berhasil diambil", Toast.LENGTH_SHORT)
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
                            this@EditMemberGym,
                            errors.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        Toast.makeText(this@EditMemberGym, e.message, Toast.LENGTH_SHORT).show()
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

    private fun createMahasiswa() {
        setLoading(true)

        if (etPersonalTrainer!!.text.toString().isEmpty()) {
            Toast.makeText(
                this@EditMemberGym,
                "Personal Trainer tidak boleh kosong!",
                Toast.LENGTH_SHORT
            ).show()
        } else if (etMembership!!.text.toString().isEmpty()) {
            Toast.makeText(this@EditMemberGym, "Membership tidak boleh kosong!", Toast.LENGTH_SHORT)
                .show()
        } else if (edTanggal!!.text.toString().isEmpty()) {
            Toast.makeText(this@EditMemberGym, "Tanggal tidak boleh kosong!", Toast.LENGTH_SHORT)
                .show()
        } else if (edDurasi!!.text.toString().isEmpty()) {
            Toast.makeText(this@EditMemberGym, "Durasi tidak boleh kosong!", Toast.LENGTH_SHORT)
                .show()
        } else {

            val member = MemberGymModel(
                0,
                etPersonalTrainer!!.text.toString(),
                etMembership!!.text.toString(),
                edTanggal!!.text.toString(),
                edDurasi!!.text.toString()
            )
            val stringRequest: StringRequest =
                object :
                    StringRequest(Method.POST, MemberGymApi.ADD_URL, Response.Listener { response ->
                        val gson = Gson()
                        val mahasiswa = gson.fromJson(response, MemberGym::class.java)

                        if (mahasiswa != null)
                            Toast.makeText(
                                this@EditMemberGym,
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
                                this@EditMemberGym,
                                errors.getString("message"),
                                Toast.LENGTH_SHORT
                            ).show()
                        } catch (e: Exception) {
                            Toast.makeText(this@EditMemberGym, e.message, Toast.LENGTH_SHORT).show()
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
                        val requestBody = gson.toJson(member)
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

    private fun updateMahasiswa(id: Int) {
        setLoading(true)

        val member = MemberGymModel(
            id,
            etPersonalTrainer!!.text.toString(),
            etMembership!!.text.toString(),
            edTanggal!!.text.toString(),
            edDurasi!!.text.toString()
        )

        val stringRequest: StringRequest = object :
            StringRequest(
                Method.PUT,
                MemberGymApi.UPDATE_URL + id,
                Response.Listener { response ->
                    val gson = Gson()

                    val mahasiswa = gson.fromJson(response, MemberGymModel::class.java)

                    if (mahasiswa != null)
                        Toast.makeText(
                            this@EditMemberGym,
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
                            this@EditMemberGym,
                            errors.getString("message"),
                            Toast.LENGTH_SHORT
                        ).show()
                    } catch (e: Exception) {
                        Toast.makeText(this@EditMemberGym, e.message, Toast.LENGTH_SHORT).show()
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
                val requestBody = gson.toJson(member)
                return requestBody.toByteArray(StandardCharsets.UTF_8)
            }

            override fun getBodyContentType(): String {
                return "application/json"
            }
        }
        queue!!.add(stringRequest)
    }

    //Fungsi ini digunakan untuk menampilkan layout Loading


        @SuppressLint("ObsoleteSdkInt")
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Throws(
            FileNotFoundException::class
        )
        private fun createPdf(
            personalTrainer: String,
            membership: String,
            tanggal: String,
            durasi: String
        ) {
            //akses writing ke storage hp dalam mode download
            val pdfPath =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
                    .toString()
            val file = File(pdfPath, "pdf_17.pdf")
            FileOutputStream(file)

            //inisialisasi pembuatan PDF
            val writer = PdfWriter(file)
            val pdfDocument = PdfDocument(writer)
            val document = Document(pdfDocument)
            pdfDocument.defaultPageSize = PageSize.A4
            document.setMargins(5f, 5f, 5f, 5f)
            @SuppressLint("useCompatLoadingForDrawables") val d = getDrawable(R.drawable.gym)

            //penambahan gambar
            val bitmap = (d as BitmapDrawable)!!.bitmap
            val stream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val bitmapData = stream.toByteArray()
            val imageData = ImageDataFactory.create(bitmapData)
            val image = Image(imageData)
            val namapengguna = Paragraph("Identitas Pengguna").setBold().setFontSize(24f)
                .setTextAlignment(TextAlignment.CENTER)
            val group = Paragraph(
                """
                                    Berikut adalah
                                    Nama Pengguna UAJY 2022/2023
                                """.trimIndent()
            ).setTextAlignment(TextAlignment.CENTER).setFontSize(12f)

            //pembuatan table
            val width = floatArrayOf(100f, 100f)
            val table = Table(width)
            //pengisian data ke dalam tabel
            table.setHorizontalAlignment(HorizontalAlignment.CENTER)
            table.addCell(Cell().add(Paragraph("Personal Trainer")))
            table.addCell(Cell().add(Paragraph(personalTrainer)))
            table.addCell(Cell().add(Paragraph("Membership")))
            table.addCell(Cell().add(Paragraph(membership)))
            table.addCell(Cell().add(Paragraph("Tanggal")))
            table.addCell(Cell().add(Paragraph(tanggal)))
            table.addCell(Cell().add(Paragraph("Durasi")))
            table.addCell(Cell().add(Paragraph(durasi)))
            val dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
            table.addCell(Cell().add(Paragraph("Tanggal Buat PDF")))
            table.addCell(Cell().add(Paragraph(LocalDate.now().format(dateTimeFormatter))))
            val timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss a")
            table.addCell(Cell().add(Paragraph("Pukul Pembuatan")))
            table.addCell(Cell().add(Paragraph(LocalTime.now().format(timeFormatter))))

            //pembuatan QR CODE secara generate dengan bantuan IText7
            val barcodeQRCode = BarcodeQRCode(
                """
            $personalTrainer
            $membership
            $tanggal
            $durasi
            ${LocalDate.now().format(dateTimeFormatter)}
            ${LocalTime.now().format(timeFormatter)}
        """.trimIndent()
            )
            val qrCodeObject = barcodeQRCode.createFormXObject(ColorConstants.BLACK, pdfDocument)
            val qrCodeImage = Image(qrCodeObject).setWidth(80f).setHorizontalAlignment(
                HorizontalAlignment.CENTER
            )

            document.add(image)
            document.add(namapengguna)
            document.add(group)
            document.add(table)
            document.add(qrCodeImage)

            document.close()
            Toast.makeText(this, "Pdf Created", Toast.LENGTH_SHORT).show()
        }

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