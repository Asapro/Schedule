package com.engineer.android.library.handler;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.text.TextUtils;
import android.util.Base64;

import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.HashSet;
import java.util.Set;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * Created by L.J on 2016/4/9.
 */
public final class SharedPreferenceHandler {
    public static SharedPreferenceHandler newInstance(Context context, String name){
        return new SharedPreferenceHandler(name, context);
    }

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private SharedPreferenceHandler(String name, Context context){
        this.sharedPreferences = context.getSharedPreferences(name,Context.MODE_PRIVATE);
        this.editor = this.sharedPreferences.edit();
        this.editor.apply();
    }

    private SecretKeySpec generateSecretKeySpec(String seed) throws NoSuchAlgorithmException, NoSuchProviderException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
        SecureRandom secureRandom = Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN ? SecureRandom.getInstance("SHA1PRNG","Crypto"):SecureRandom.getInstance("SHA1PRNG");
        secureRandom.setSeed(seed.getBytes());
        keyGenerator.init(128,secureRandom);
        return new SecretKeySpec(keyGenerator.generateKey().getEncoded(),"AES");
    }

    private String encrypt(String source,String seed) {
        String target = null;
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/ISO10126Padding");
            cipher.init(Cipher.ENCRYPT_MODE,generateSecretKeySpec(seed),new IvParameterSpec(new byte[16]));
            target = Base64.encodeToString(cipher.doFinal(source.getBytes()),Base64.DEFAULT);
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | NoSuchProviderException | InvalidKeyException | BadPaddingException | IllegalBlockSizeException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return target;
    }

    private String decrypt(String source,String seed) {
        String target = null;
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/ISO10126Padding");
            cipher.init(Cipher.DECRYPT_MODE,generateSecretKeySpec(seed),new IvParameterSpec(new byte[16]));
            target = new String(Base64.decode(cipher.doFinal(source.getBytes()),Base64.DEFAULT));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | NoSuchProviderException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
        return target;
    }

    public boolean putEncryptString(String key,String value,String seed) {
        String encryptValue = encrypt(value, seed);
        return !TextUtils.isEmpty(encryptValue) && this.editor.putString(key, encryptValue).commit();
    }

    public String getDecryptString(String key,String defaultValue,String seed){
        String value = this.sharedPreferences.getString(key,null);
        if(TextUtils.isEmpty(value)){
            return defaultValue;
        }else{
            String decryptValue = decrypt(value,seed);
            if(TextUtils.isEmpty(decryptValue)){
                return defaultValue;
            }else{
                return decryptValue;
            }
        }
    }

    public boolean putEncryptInt(String key,int value,String seed){
        String encryptValue = encrypt(String.valueOf(value),seed);
        return !TextUtils.isEmpty(encryptValue) && this.editor.putString(key,encryptValue).commit();
    }

    public int getDecryptInt(String key,int defaultValue,String seed){
        String value = this.sharedPreferences.getString(key,null);
        if(TextUtils.isEmpty(value)){
            return defaultValue;
        }else{
            String decryptValue = decrypt(value,seed);
            if(TextUtils.isEmpty(decryptValue)){
                return defaultValue;
            }else {
                return Integer.valueOf(decryptValue);
            }
        }
    }

    public boolean putEncryptFloat(String key,float value,String seed) {
        String encryptValue = encrypt(String.valueOf(value),seed);
        return !TextUtils.isEmpty(encryptValue) && this.editor.putString(key,encryptValue).commit();
    }

    public float getDecryptFloat(String key,float defaultValue,String seed) {
        String value = this.sharedPreferences.getString(key,null);
        if(TextUtils.isEmpty(value)){
            return defaultValue;
        }else {
            String decryptValue = decrypt(value,seed);
            if(TextUtils.isEmpty(decryptValue)){
                return defaultValue;
            }else{
                return Float.valueOf(decryptValue);
            }
        }
    }

    public boolean putEncryptLong(String key,long value,String seed) {
        String encryptValue = encrypt(String.valueOf(value),seed);
        return !TextUtils.isEmpty(encryptValue) && this.editor.putString(key,encryptValue).commit();
    }

    public long getDecryptValue(String key,long defaultValue,String seed){
        String value = this.sharedPreferences.getString(key,null);
        if(TextUtils.isEmpty(value)){
            return defaultValue;
        }else {
            String decryptValue = decrypt(value,seed);
            if(TextUtils.isEmpty(decryptValue)){
                return defaultValue;
            }else {
                return Long.valueOf(decryptValue);
            }
        }
    }

    public boolean putEncryptStringSet(String key,Set<String> value,String seed){
        HashSet<String> hashSet = new HashSet<>();
        for (String aValue : value) {
            String encryptValue = encrypt(aValue,seed);
            if(TextUtils.isEmpty(encryptValue)){
                continue;
            }
            hashSet.add(encrypt(aValue, seed));
        }
        return !hashSet.isEmpty() && this.editor.putStringSet(key,hashSet).commit();
    }

    public Set<String> getDecryptStringSet(String key,Set<String> defaultValue,String seed){

        Set<String> value = this.sharedPreferences.getStringSet(key,null);
        if(value == null){
            return defaultValue;
        }else{
            HashSet<String> hashSet = new HashSet<>();
            for (String aValue : value) {
                String decryptValue = decrypt(aValue, seed);
                if (TextUtils.isEmpty(decryptValue)) {
                    continue;
                }
                hashSet.add(decryptValue);
            }
            if(hashSet.isEmpty()){
                return defaultValue;
            }else{
                return hashSet;
            }
        }
    }

    public void putString(String key,String value){
        this.editor.putString(key, value).apply();
    }

    public String getString(String key,String defaultValue){
        return this.sharedPreferences.getString(key, defaultValue);
    }

    public void putBoolean(String key,boolean value) {
        this.editor.putBoolean(key, value).apply();
    }

    public boolean getBoolean(String key,boolean defaultValue) {
        return this.sharedPreferences.getBoolean(key, defaultValue);
    }

    public void putFloat(String key,float value) {
        this.editor.putFloat(key, value).apply();
    }

    public float getFloat(String key,float defaultValue) {
        return this.sharedPreferences.getFloat(key, defaultValue);
    }

    public void putLong(String key,long value){
        this.editor.putLong(key, value).apply();
    }

    public long getLong(String key,long defaultValue) {
        return this.sharedPreferences.getLong(key, defaultValue);
    }

    public void putStringSet(String key,Set<String> value) {
        this.editor.putStringSet(key, value).apply();
    }

    public Set<String> getStringSet(String key,Set<String> defaultValue){
        return this.sharedPreferences.getStringSet(key, defaultValue);
    }
}
