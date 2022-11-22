<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Validation\Rule;
use Illuminate\Support\Facades\Validator;
use App\Models\memberGym;

class memberGymController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        //
        $memberData = memberGym::all();

        if(count($memberData) > 0){
        return response([
                'message' => 'Retrieve All Success',
                'data' => $memberData
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
            'personalTrainer' => 'required',
            'membership' => 'required',
            'tanggal' => 'required',
            'durasi' => 'required'
        ]);

        if($validate->fails()) 
            return response(['message' => $validate->errors()], 400);

        $product = memberGym::create($storeData);
        return response([
            'message' => 'Add Member Success',
            'data' => $product
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
        $memberData = memberGym::find($id);
        if(!is_null($memberData)){
            return response([
                'message' => 'Retrieve User Success',
                'data' => $memberData
            ], 200);
        }

        return response([
            'message' => 'User Not Found',
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
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function update(Request $request, $id)
    {
        //
        $userData = memberGym::find($id);

        if(is_null($userData)){
            return response([
                'message' => 'Member Not Found',
                'data' => null
            ], 404);
        }

        $updateData = $request->all();
        $validate = Validator::make($updateData, [
            'personalTrainer' => 'required',
            'membership' => 'required',
            'tanggal' => 'required',
            'durasi' => 'required'
        ]);

        if($validate->fails()) 
            return response(['message' => $validate->errors()], 400);

        $userData->personalTrainer = $updateData['personalTrainer'];
        $userData->membership = $updateData['membership'];
        $userData->tanggal = $updateData['tanggal'];
        $userData->durasi = $updateData['durasi'];

        if($userData->save()){
             return response([
                'message'=> 'Update Member Success',
                'data' => $userData
             ], 200);
        }

        return response([
            'message'=> 'Update User Failed',
            'data' => null
        ], 400);
    }

    /**
     * Remove the specified resource from storage.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function destroy($id)
    {
        //
        $memberData = memberGym::find($id);

        if(is_null($memberData)){
            return response([
                'message' => 'Member Not Found',
                'data' => null
            ], 404);
        }

        if($memberData->delete()){
            return response([
                'message' => 'Delete Member Success',
                'data' => $memberData
            ], 200);
        }

        return response([
            'message' => 'Delete Member Failed',
            'data' => null
        ], 400);
    }
}
