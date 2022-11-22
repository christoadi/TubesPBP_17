<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Validation\Rule;
use Illuminate\Support\Facades\Validator;
use App\Models\profile;

class profileController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        //
        $userData = profile::all();

        if(count($userData) > 0){
        return response([
                'message' => 'Retrieve All Success',
                'data' => $userData
            ], 200);
        }

        return response([
            'message' => 'Empty',
            'data' => null
        ], 400);
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
        $userData = profile::find($id);
        if(!is_null($userData)){
            return response([
                'message' => 'Retrieve User Success',
                'data' => $userData
            ], 200);
        }

        return response([
            'message' => 'User Not Found',
            'data' => null
        ],404);
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
        $userData = profile::find($id);

        if(is_null($userData)){
            return response([
                'message' => 'User Not Found',
                'data' => null
            ], 404);
        }

        $updateData = $request->all();
        $validate = Validator::make($updateData, [
            'username' => 'required',
            'password' => 'required',
            'email' => 'required',
            'tanggalLahir' => 'required',
            'nomorTelepon' => 'required'
        ]);

        if($validate->fails()) 
            return response(['message' => $validate->errors()], 400);

        $userData->username = $updateData['username'];
        $userData->password = $updateData['password'];
        $userData->email = $updateData['email'];
        $userData->tanggalLahir = $updateData['tanggalLahir'];
        $userData->nomorTelepon = $updateData['nomorTelepon'];


        if($userData->save()){
             return response([
                'message'=> 'Update User Success',
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
        $userData = profile::find($id);

        if(is_null($userData)){
            return response([
                'message' => 'User Not Found',
                'data' => null
            ], 404);
        }

        if($userData->delete()){
            return response([
                'message' => 'Delete User Success',
                'data' => $userData
            ], 200);
        }

        return response([
            'message' => 'Delete userData Failed',
            'data' => null
        ], 400);
    }
}
