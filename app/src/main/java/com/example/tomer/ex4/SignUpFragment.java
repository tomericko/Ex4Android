package com.example.tomer.ex4;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import java.io.IOException;

public class SignUpFragment extends Fragment {

    private View rootView;
    private EditText userNameTxt;
    private EditText passwordTxt;
    private EditText emailTxt;
    private EditText nameTxt;
    private ImageButton usernameResetBtn;
    private ImageButton passwordResetBtn;
    private ImageButton emailResetBtn;
    private ImageButton nameResetBtn;
    private ImageView iconView;
    private Button signUpBtn;
    private Bitmap profileBitmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null)
        {
            profileBitmap = savedInstanceState.getParcelable("profileBitmap");
        }
        else
        {
            Drawable drawable = getResources().getDrawable(R.drawable.defaultprofile);
            profileBitmap = ((BitmapDrawable)drawable).getBitmap();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.activity_sign_up,container, false);
        signUpBtn = (Button) rootView.findViewById(R.id.signUpBtn);
        iconView = (ImageView)rootView.findViewById(R.id.iconView);
        userNameTxt = (EditText)rootView.findViewById(R.id.usernameTxt);
        passwordTxt = (EditText)rootView.findViewById(R.id.passwordTxt);
        emailTxt = (EditText)rootView.findViewById(R.id.emailTxt);
        nameTxt = (EditText)rootView.findViewById(R.id.nameTxt);
        usernameResetBtn = (ImageButton) rootView.findViewById(R.id.usernameResetBtn);
        passwordResetBtn = (ImageButton) rootView.findViewById(R.id.passwordResetBtn);
        emailResetBtn = (ImageButton) rootView.findViewById(R.id.emailResetBtn);
        nameResetBtn = (ImageButton) rootView.findViewById(R.id.nameResetBtn);
        usernameResetBtn.setVisibility(View.INVISIBLE);
        passwordResetBtn.setVisibility(View.INVISIBLE);
        emailResetBtn.setVisibility(View.INVISIBLE);
        nameResetBtn.setVisibility(View.INVISIBLE);

        setRemovableET(userNameTxt, usernameResetBtn);
        setRemovableET(passwordTxt, passwordResetBtn);
        setRemovableET(emailTxt, emailResetBtn);
        setRemovableET(nameTxt, nameResetBtn);

        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSignup();
            }
        });
        iconView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        Animation anim = AnimationUtils.loadAnimation(getContext(), R.anim.fade_in);
        iconView.startAnimation(anim);
        setButtonEnabled(signUpBtn, false);
        setProfilePicture(profileBitmap);
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSaveInstanceState(Bundle toSave) {
        super.onSaveInstanceState(toSave);
        toSave.putParcelable("profileBitmap", profileBitmap);
    }

    private void attemptSignup(){
        boolean cancel = false;
        View focusView = null;


        String userName = userNameTxt.getText().toString();
        String password = passwordTxt.getText().toString();
        String email = emailTxt.getText().toString();



    }

    private void selectImage() {

        final CharSequence[] options = { "Take Photo", "Choose from Gallery", "Set default profile", "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo"))
                {
                    Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    //takePicture.putExtra(MediaStore.EXTRA_OUTPUT, "profile.jpg");
                    startActivityForResult(takePicture, 0);
                }
                else if (options[item].equals("Choose from Gallery"))
                {
                    Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(pickPhoto , 1);
                }
                else if (options[item].equals("Set default profile")) {
                    Drawable drawable = getResources().getDrawable(R.drawable.defaultprofile);
                    profileBitmap = ((BitmapDrawable)drawable).getBitmap();
                    setProfilePicture(profileBitmap);
                }
                else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        switch(requestCode) {
            case 0:
                if(resultCode == getActivity().RESULT_OK){
                    profileBitmap = (Bitmap) imageReturnedIntent.getExtras().get("data");
                    setProfilePicture(profileBitmap);
                }
                break;
            case 1:
                if(resultCode == getActivity().RESULT_OK){
                    Uri selectedImage = imageReturnedIntent.getData();
                    try {
                        profileBitmap = MediaStore.Images.Media.getBitmap(this.getContext().getContentResolver(), selectedImage);
                        setProfilePicture(profileBitmap);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }

    private void setRemovableET(final EditText et, final ImageView resetIB) {

        et.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && et.getText().toString().length() > 0)
                    resetIB.setVisibility(View.VISIBLE);
                else
                    resetIB.setVisibility(View.INVISIBLE);
            }
        });

        resetIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                et.setText("");
                resetIB.setVisibility(View.INVISIBLE);
            }
        });

        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s.length() != 0){
                    resetIB.setVisibility(View.VISIBLE);
                }else{
                    resetIB.setVisibility(View.INVISIBLE);
                }
                if (userNameTxt.getText().toString().equals("") || passwordTxt.getText().toString().equals("") ||
                        emailTxt.getText().toString().equals("") || nameTxt.getText().toString().equals(""))
                {
                    setButtonEnabled(signUpBtn, false);
                }
                else
                {
                    setButtonEnabled(signUpBtn, true);
                }
            }
        });
    }

    private void setProfilePicture(Bitmap profileBitmap)
    {
        Bitmap circleBitmap = Bitmap.createBitmap(profileBitmap.getWidth(), profileBitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(circleBitmap);
        BitmapShader shader = new BitmapShader(profileBitmap,  Shader.TileMode.CLAMP, Shader.TileMode.CLAMP);
        Paint paint = new Paint();
        paint.setShader(shader);
        c.drawCircle(profileBitmap.getWidth()/2, profileBitmap.getHeight()/2, profileBitmap.getWidth()/2, paint);
        iconView.setImageBitmap(circleBitmap);
    }

    private Bitmap decodeFile(Uri uriFile) throws OutOfMemoryError {
        String filePath = uriFile.getPath();
        BitmapFactory.Options bmOptions;
        Bitmap imageBitmap;
        try {
            imageBitmap = BitmapFactory.decodeFile(filePath);
        } catch (OutOfMemoryError e) {
            bmOptions = new BitmapFactory.Options();
            bmOptions.inSampleSize = 4;
            bmOptions.inPurgeable = true;
            imageBitmap = BitmapFactory.decodeFile(filePath, bmOptions);
        }
        return imageBitmap;
    }

    private void setButtonEnabled(Button btn, boolean enable)
    {
        if (enable)
        {
            btn.setBackgroundResource(R.drawable.enablebtn);
        }
        else
        {
            btn.setBackgroundResource(R.drawable.disablebtn);
        }
        btn.setEnabled(enable);
    }
}

