package edu.cnm.deepdive.geoquiz;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Quiz_Activity extends AppCompatActivity {
  private static final String TAG = "QuizActivity";
  private static final String KEY_INDEX = "index";
  private static final int REQUEST_CODE_CHEAT = 0;

  private Button mTrueButton;
  private Button mFalseButton;
  private Button mNextButton;
  private Button mCheatButton;
  private TextView mQuestionTextView;
  private TextView mCheatsRemaining;
  private int numberCheats;

  private Question[] mQuestionBank = new Question[] {
      new Question(R.string.question_oceania, true),
      new Question(R.string.question_oceans, true),
      new Question(R.string.question_mideast, false),
      new Question(R.string.question_africa, false),
      new Question(R.string.question_americas, true),
      new Question(R.string.question_asia, true),
  };

  private int mCurrentIndex = 0;
  private boolean mIsCheater;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d(TAG, "onCreate(Bundle) called");
    setContentView(R.layout.activity_quiz_);

    if (savedInstanceState != null) {
      mCurrentIndex = savedInstanceState.getInt(KEY_INDEX, 0);
    }

    mQuestionTextView = findViewById(R.id.question_text_view);

    mTrueButton = findViewById(R.id.true_button);
    mTrueButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        checkAnswer(true);
      }
    });

    mFalseButton = findViewById(R.id.false_button);
    mFalseButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        checkAnswer(false);
      }
    });

    mNextButton = findViewById(R.id.next_button);
    mNextButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        mCurrentIndex = (mCurrentIndex + 1) % mQuestionBank.length;
        mIsCheater = false;
        updateQuestion();
      }
    });

    mCheatsRemaining = findViewById(R.id.cheats_remain);
    numberCheats = 3;

    mCheatButton = findViewById(R.id.cheat_button);
    mCheatButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        mCheatsRemaining.setText("Cheats Remaining " + numberCheats);
        if (numberCheats > 0) {
          boolean answerIsTrue = mQuestionBank[mCurrentIndex].ismAnswerTrue();
          numberCheats--;
          Intent intent = CheatActivity.newIntent(Quiz_Activity.this, answerIsTrue);
          startActivityForResult(intent, REQUEST_CODE_CHEAT);
        } else {
          Toast.makeText(Quiz_Activity.this, R.string.too_much_cheating, Toast.LENGTH_SHORT).show();
        }
      }
    });

    updateQuestion();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (resultCode != Activity.RESULT_OK) {
      return;
    }

    if (requestCode == REQUEST_CODE_CHEAT) {
      if (data == null) {
        return;
      }

      mIsCheater = CheatActivity.wasAnswerShown(data);
    }
  }

  @Override
  protected void onStart() {
    super.onStart();
    Log.d(TAG, "onStart() called");
  }

  @Override
  protected void onStop() {
    super.onStop();
    Log.d(TAG, "onStop() called");
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    Log.d(TAG, "onDestroy() called");
  }

  @Override
  protected void onPause() {
    super.onPause();
    Log.d(TAG, "onPause() called");
  }

  @Override
  public void onSaveInstanceState(Bundle savedInstanceState) {
    super.onSaveInstanceState(savedInstanceState);
    Log.i(TAG, "onSaveInstanceState");
    savedInstanceState.putInt(KEY_INDEX, mCurrentIndex);
  }

  @Override
  protected void onResume() {
    super.onResume();
    Log.d(TAG, "onResume called");
  }

  private void updateQuestion() {
    int question = mQuestionBank[mCurrentIndex].getmTextResId();
    mQuestionTextView.setText(question);
  }

  private void checkAnswer(boolean userPressedTrue) {
    boolean answerIsTrue = mQuestionBank[mCurrentIndex].ismAnswerTrue();
    int messageResId;
    if(mIsCheater) {
      messageResId = R.string.judgement_toast;
    } else {
      if (userPressedTrue == answerIsTrue) {
        messageResId = R.string.correct_toast;
      } else {
        messageResId = R.string.incorrect_toast;
      }
    }

    Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show();
  }
}
