package com.hong.fragement.MovieDetailPage;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hong.fragement.MyPage.MemberObj;
import com.hong.fragement.R;

public class CustomDialog extends Dialog {

    private RatingObj obj;
    private RatingBar ratingBar;
    private TextInputLayout ti_review;
    private TextInputEditText edit_review;
    private  Button mAddButton;
    private Button mCancelButton;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference ratingRef = db.collection("Rating");
    private String movieTitle;;

    private FirebaseAuth mFirebaseAuth ;
    FirebaseUser mFirebaseUser;

    private MemberObj memberObj;

    public CustomDialog(Context context, String movieTitle) {
        super(context);
        this.movieTitle = movieTitle;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        layoutParams.flags = WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        layoutParams.dimAmount = 0.8f;
        getWindow().setAttributes(layoutParams);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        obj = new RatingObj();

        if(mFirebaseUser != null) {
            DocumentReference docRef = db.collection("Users").document(mFirebaseUser.getUid());
            docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    memberObj = documentSnapshot.toObject(MemberObj.class);
                    obj.setId(memberObj.getEmail());
                }
            });
        }

        setContentView(R.layout.detail_movie_activity_custom_dialog);

        ratingBar = findViewById(R.id.rating_bar);
        ti_review = findViewById(R.id.ti_review);
        edit_review =  findViewById(R.id.edit_review);
        mAddButton = findViewById(R.id.add_btn);
        mCancelButton = findViewById(R.id.cancel_btn);

        ti_review.setCounterEnabled(true);
        ti_review.setCounterMaxLength(100);

        mAddButton.setOnClickListener(new View.OnClickListener() {  // 리뷰추가버튼 리스너
            @Override
            public void onClick(View view) {

                obj.setScore(ratingBar.getRating());
                obj.setReview(edit_review.getText().toString());

                Log.i("리뷰 데이터를 가져옵니다","평가점수는"+ratingBar
                        +"리뷰 내용은"+ edit_review.getText().toString());

                if(ratingBar.getRating() < 0.5) {
                    Toast.makeText(getContext(),"별을 올려주세요",Toast.LENGTH_SHORT).show();

                } else if(edit_review.getText().toString() == null) {
                    Toast.makeText(getContext(),"리뷰를 작성해주세요",Toast.LENGTH_SHORT).show();
                    edit_review.requestFocus();
                } else {
                    String addDBOrder = Integer.toString(DetailMovieActivity.dataList.size()+1); // DB에 (데이터 사이즈 + 1)의 숫자로 doc 저장
                    ratingRef.document(movieTitle).collection("review").document(addDBOrder).set(obj);
                    DetailMovieActivity.dataList.add(obj);
                    dismiss();
                }

            }
        });

        mCancelButton.setOnClickListener(new View.OnClickListener() {   // 취소버튼 리스너
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"다이얼로그를종료합니다",
                        Toast.LENGTH_SHORT).show();
                dismiss();
            }
        });
    }

}