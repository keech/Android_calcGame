package com.example.hurryup;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.res.Resources;
import android.os.SystemClock;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;

public class HurryUpActivity extends Activity {

    static final int imageBtnIds[] = {
        R.id.num_btn1, R.id.num_btn2, R.id.num_btn3, R.id.num_btn4,
        R.id.num_btn5, R.id.num_btn6, R.id.num_btn7, R.id.num_btn8,
        R.id.num_btn9, R.id.num_btn10, R.id.num_btn11, R.id.num_btn12,
    };

    static final int numImages[] = {
        R.drawable.num1, R.drawable.num2, R.drawable.num3, R.drawable.num4,
    R.drawable.num5, R.drawable.num6, R.drawable.num7, R.drawable.num8,
        R.drawable.num9, R.drawable.num10, R.drawable.num11, R.drawable.num12,
    };

    static final int opeBtnIds[] = {
        R.id.ope_btn1, R.id.ope_btn2, R.id.ope_btn3, R.id.ope_btn4
    };

    static final int opeImages[] = {
        R.drawable.plus, R.drawable.minus, R.drawable.mul, R.drawable.div
    };

    private int _sum = 0;
    private int _answeredCnt = 0;
    private int _correctAnswerCnt = 0;
    private int _leftMember = 0;
    private int _rightMember = 0;
    private int _ope = 0;

    AnswerNum nums[] = new AnswerNum[imageBtnIds.length];
    AnswerOpe opes[] = new AnswerOpe[opeBtnIds.length];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hurry_up);
        _createNums();
        _createOpes();
        _setStartButtnListener();
    }

    private void _createNums() {
        for (int i = 0; i < imageBtnIds.length; ++i) {
            ImageButton imgBtn = (ImageButton) findViewById(imageBtnIds[i]);
            nums[i] = new AnswerNum(imgBtn, i);
        }
    }

    private void _createOpes() {
        for (int i = 0; i < opeBtnIds.length; ++i) {
            ImageButton imgBtn = (ImageButton) findViewById(opeBtnIds[i]);
            opes[i] = new AnswerOpe(imgBtn, i);
        }
    }

    private void _setStartButtnListener() {
        Button btn = (Button) findViewById(R.id.start_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                _startGame();
            }
        });
    }

    private void _startGame() {
        _answeredCnt = 0;
        _correctAnswerCnt = 0;
        TextView textResult = (TextView) findViewById(R.id.text_result);
        textResult.setText("");
        _startChronometer();
        _syutudai();
    }

    private void _syutudai() {
        _clearAnswer();
        int a;
        int b;
        int swap;
        int ope;
        while (true) {
            a = (int) (Math.random() * 12 + 1);
            b = (int) (Math.random() * 12 + 1);
            ope = (int) (Math.random() * 4 + 1);
            if ((ope % 2) == 0) {
                swap = b;
                b = a;
                a = swap;
            }
            if ((ope % 4) == 4) {
                if ((a % b) != 0) {
                    continue;
                }
            }
            break;
        }
        switch (ope) {
            case 1:
                _sum = a + b;
                break;
            case 2:
                _sum = a - b;
                break;
            case 3:
                _sum = a * b;
                break;
            case 4:
                _sum = a / b;
                break;
        }
        Resources res = getResources();
        TextView textAnswer = (TextView) findViewById(R.id.text_answer);
        String ansString = res.getString(R.string.answer_is) + " " + _sum;
        textAnswer.setText(ansString);
    }

    private void _clearAnswer() {
        _leftMember = 0;
        _rightMember = 0;
        _ope = 0;

        ImageButton imgBtn = (ImageButton) findViewById(R.id.a_btn);
        imgBtn.setImageResource(R.drawable.clear);

        imgBtn = (ImageButton) findViewById(R.id.x_btn);
        imgBtn.setImageResource(R.drawable.clear);

        imgBtn = (ImageButton) findViewById(R.id.b_btn);
        imgBtn.setImageResource(R.drawable.clear);

        Button btn = (Button) findViewById(R.id.c_btn);
        btn.setText("");
    }

    private void _startChronometer() {
        Chronometer chrono = (Chronometer) findViewById(R.id.chronometer);
        chrono.setBase(SystemClock.elapsedRealtime());
        chrono.start();
    }

    private void _stopChronometer() {
        Chronometer chrone = (Chronometer) findViewById(R.id.chronometer);
        chrone.stop();
    }

    private void _hantei() {
        if (_leftMember == 0) return;
        if (_rightMember == 0) return;
        if (_ope == 0) return;
        int answer = 0;
        int mod = 0;
        boolean correct = false;

        switch (_ope) {
            case 1:
                answer = _leftMember + _rightMember;
                break;
            case 2:
                answer = _leftMember - _rightMember;
                break;
            case 3:
                answer = _leftMember * _rightMember;
                break;
            case 4:
                answer = _leftMember / _rightMember;
                mod = _leftMember % _rightMember;
                break;
        }
        if (answer == _sum) {
            if (_ope != 4) {
                correct = true;
                _correctAnswerCnt++;
            } else {
                if (mod == 0) {
                    correct = true;
                    _correctAnswerCnt++;
                }
            }
        }
        if (mod != 0) {
            Button btn = (Button) findViewById(R.id.c_btn);
            btn.setText("あまり");
        } else {
            Button btn = (Button) findViewById(R.id.c_btn);
            btn.setText(String.valueOf(answer));
        }
        showDialog(correct);
    }

    void showDialog(boolean flag) {
        if (flag) {
            DialogFragment newFragment = MyAlertDialogFragment.newInstance(R.string.hantei, R.string.correct);
            newFragment.show(getFragmentManager(), "dialog");
        }else{
            DialogFragment newFragment = MyAlertDialogFragment.newInstance(R.string.hantei, R.string.incorrect);
            newFragment.show(getFragmentManager(), "dialog");
        }
    }

    public void doPositiveClick() {
        if (++_answeredCnt < 5) {
            _syutudai();
        } else {
            _complete();
        }
    }

    private void _complete() {
        _stopChronometer();
        Resources res = getResources();
        String resultString = res.getString(R.string.result_is) + " " + _correctAnswerCnt;
        TextView textResult = (TextView) findViewById(R.id.text_result);
        textResult.setText(resultString);
    }

    //////////////////////////////////////////////////////////////////////
    class AnswerNum implements View.OnClickListener {
        int MyIdx = 0;

        public AnswerNum(ImageButton btn, int i) {
            MyIdx = i;
            btn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (_leftMember == 0) {
                ImageButton imgBtn = (ImageButton) findViewById(R.id.a_btn);
                imgBtn.setImageResource(numImages[MyIdx]);
                _leftMember = MyIdx + 1;
            } else if (_rightMember == 0) {
                ImageButton imgBtn = (ImageButton) findViewById(R.id.b_btn);
                imgBtn.setImageResource(numImages[MyIdx]);
                _rightMember = MyIdx + 1;
                _hantei();
            }
        }
    }

    //////////////////////////////////////////////////////////////////////
    class AnswerOpe implements View.OnClickListener {
        int MyIdx = 0;

        public AnswerOpe(ImageButton btn, int i) {
            MyIdx = i;
            btn.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (_ope == 0) {
                ImageButton imgBtn = (ImageButton) findViewById(R.id.x_btn);
                imgBtn.setImageResource(opeImages[MyIdx]);
                _ope = MyIdx + 1;
                _hantei();
            }
        }
    }
}
