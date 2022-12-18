<?php

namespace App\Http\Controllers;

use App\Models\kritikSaran;
use Illuminate\Support\Facades\Validator;
use Illuminate\Http\Request;

class kritikSaranController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        //
        $kritikSarans = kritikSaran::all();

        if(count($kritikSarans) > 0){
        return response([
                'message' => 'Retrieve All Success',
                'data' => $kritikSarans
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
            'nama' => 'required',
            'kritik' => 'required',
            'saran' => 'required',
            'nomorTelepon' => 'required'
        ]);

        if($validate->fails()) 
            return response(['message' => $validate->errors()], 400);

        $kritikSarans = kritikSaran::create($storeData);
        return response([
            'message' => 'Add Kritik Saran Success',
            'data' => $kritikSarans
        ],200);
    }

    /**
     * Display the specified resource.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function show($id)
    {
        //
        $kritikSarans = kritikSaran::find($id);
        if(!is_null($kritikSarans)){
            return response([
                'message' => 'Retrieve Kritik Saran Success',
                'data' => $kritikSarans
            ], 200);
        }

        return response([
            'message' => 'Kritik Saran Not Found',
            'data' => null
        ],404);
    }

    /**
     * Show the form for editing the specified resource.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function edit($id)
    {
        //
    }

    /**
     * Update the specified resource in storage.
     *
     * @param  \Illuminate\Http\Request  $request
     * @param  \App\Models\kritikSaran
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, $id)
    {
        //
        $kritikSarans = kritikSaran::find($id);

        if(is_null($kritikSarans)){
            return response([
                'message' => 'Kritik Saran Not Found',
                'data' => null
            ], 404);
        }

        $updateData = $request->all();
        $validate = Validator::make($updateData, [
            'nama' => 'required',
            'kritik' => 'required',
            'saran' => 'required',
            'nomorTelepon' => 'required'
        ]);

        if($validate->fails()) 
            return response(['message' => $validate->errors()], 400);

        $kritikSarans->nana = $updateData['nana'];
        $kritikSarans->kritik = $updateData['kritik'];
        $kritikSarans->saran = $updateData['saran'];
        $kritikSarans->nomorTelepon = $updateData['nomorTelepon'];

        if($kritikSarans->save()){
             return response([
                'message'=> 'Update Kritik Saran Success',
                'data' => $kritikSarans
             ], 200);
        }

        return response([
            'message'=> 'Update Kritik Saran Failed',
            'data' => null
        ], 400);
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  \App\Models\kritikSaran
     * @return \Illuminate\Http\Response
     */
    public function destroy($id)
    {
        //
        $kritikSarans = kritikSaran::find($id);

        if(is_null($kritikSarans)){
            return response([
                'message' => 'Kritik Saran Not Found',
                'data' => null
            ], 404);
        }

        if($kritikSarans->delete()){
            return response([
                'message' => 'Delete Kritik Saran Success',
                'data' => $kritikSarans
            ], 200);
        }

        return response([
            'message' => 'Delete Kritik Saran Failed',
            'data' => null
        ], 400);
    }
}
