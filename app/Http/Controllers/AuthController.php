<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Session;
use Illuminate\Support\Facades\Hash;
use Illuminate\Validation\Rule;
use Illuminate\Support\Facades\Validator;
use App\Models\profile;

class AuthController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return \Illuminate\Http\Response
     */
    public function index()
    {
        //
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
        $regisData = $request->all();
        $validate = Validator::make($regisData, [
            'username' => 'required',
            'password' => 'required',
            'email' => 'required',
            'tanggalLahir' => 'required',
            'nomorTelepon' => 'required'
        ]);

        if($validate->fails()) 
            return response(['message' => $validate->errors()->first()], 400);

        $regisData['password'] = bcrypt($request->password);
        $profile = profile::create($regisData);
        
        return response([
            'message' => 'Add Profile Success',
            'data' => $profile
        ],200);

    }

    public function checkLogin(Request $request){
        $loginData = $request->all();
        
        $validate = Validator::make($loginData, [
            'username' => 'required',
            'password' => 'required'
        ]);

        if($validate->fails())
            return response(['message' => $validate->errors()->first()], 400);

        $isLogin = profile::where('username', $loginData["username"])->where('password', $loginData['password'])->exists();

        if($isLogin){
            return response([
                'message' => 'Profile Found',
                'data' => profile::where('username', $loginData["username"])->where("password", $loginData["password"])->first()
            ], 200);
        }

        return response([
            'message' => 'Unauthenticated',
            'data' => null
        ], 400);
    }

    // public function logout(Request $request){
    //     $user = Auth::user()->token();
    //     $user->revoke();

    //     return response()->json([
    //         'message' => 'Success Log Out',
    //     ], 200);
    // }

    /**
     * Display the specified resource.
     *
     * @param  int  $id
     * @return \Illuminate\Http\Response
     */
    public function show($id)
    {
        //
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
    }
}
