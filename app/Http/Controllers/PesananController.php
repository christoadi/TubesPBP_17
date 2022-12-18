<?php

namespace App\Http\Controllers;

use App\Models\pesanan;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;

class PesananController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        //
        $pesanans = pesanan::all();

        if(count($pesanans) > 0){
        return response([
                'message' => 'Retrieve All Success',
                'data' => $pesanans
            ], 200);
        }

        return response([
            'message' => 'Empty',
            'data' => null
        ], 400);
    }

    /**
     * Show the form for creating a new resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function create()
    {
        //
    }

    /**
     * Store a newly created resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @return \Illuminate\Http\Response
     */
    public function store(Request $request)
    {
        //
        $storeData = $request->all();
        $validate = Validator::make($storeData, [
            'namaPemesan' => 'required',
            'namaBarang' => 'required',
            'jumlah' => 'required',
            'tanggalAmbil' => 'required'
        ]);

        if($validate->fails()) 
            return response(['message' => $validate->errors()], 400);

        $pesanans = pesanan::create($storeData);
        return response([
            'message' => 'Add Pesanan Success',
            'data' => $pesanans
        ],200);
    }

    /**
     * Display the specified resource.
     *
     * @param  int $id
     * @return \Illuminate\Http\Response
     */
    public function show($id)
    {
        //
        $pesanans = pesanan::find($id);
        if(!is_null($pesanans)){
            return response([
                'message' => 'Retrieve Pesanan Success',
                'data' => $pesanans
            ], 200);
        }

        return response([
            'message' => 'Pesanan Not Found',
            'data' => null
        ],404);
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param  \App\Models\pesanan  $pesanan
     * @return \Illuminate\Http\Response
     */
    public function edit(pesanan $pesanan)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \App\Models\pesanan  $pesanan
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, $id)
    {
        //
        $pesanans = pesanan::find($id);

        if(is_null($pesanans)){
            return response([
                'message' => 'Pesanan Not Found',
                'data' => null
            ], 404);
        }

        $updateData = $request->all();
        $validate = Validator::make($updateData, [
            'namaPemesan' => 'required',
            'namaBarang' => 'required',
            'jumlah' => 'required',
            'tanggalAmbil' => 'required'
        ]);

        if($validate->fails()) 
            return response(['message' => $validate->errors()], 400);

        $pesanans->namaPemesan = $updateData['namaPemesan'];
        $pesanans->namaBarang = $updateData['namaBarang'];
        $pesanans->jumlah = $updateData['jumlah'];
        $pesanans->tanggalAmbil = $updateData['tanggalAmbil'];

        if($pesanans->save()){
             return response([
                'message'=> 'Update Pesanan Success',
                'data' => $pesanans
             ], 200);
        }

        return response([
            'message'=> 'Update Pesanan Failed',
            'data' => null
        ], 400);
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  \App\Models\pesanan  $pesanan
     * @return \Illuminate\Http\Response
     */
    public function destroy($id)
    {
        //
        $pesanans = pesanan::find($id);

        if(is_null($pesanans)){
            return response([
                'message' => 'Pesanan Not Found',
                'data' => null
            ], 404);
        }

        if($pesanans->delete()){
            return response([
                'message' => 'Delete Pesanan Success',
                'data' => $pesanans
            ], 200);
        }

        return response([
            'message' => 'Delete Pesanan Failed',
            'data' => null
        ], 400);
    }
}
