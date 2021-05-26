package com.openclassrooms.realestatemanager.UI.Fragment.LoanSimulator;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.openclassrooms.realestatemanager.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * A simple {@link Fragment} subclass.
 */
public class LoanSimulatorFragment extends Fragment implements View.OnClickListener {

    @BindView(R.id.seekBar) SeekBar mSeekBar;
    @BindView(R.id.simulator_nbr_of_years_tv) TextView mDisplayNbrOfYears;
    @BindView(R.id.simulator_interest_tv) TextView mDisplayRateFees;
    @BindView(R.id.simulator_loan_amount_ed) EditText mLoanAmountEditText;
    @BindView(R.id.simulator_financial_contribution_ed) EditText mFinancialContributionEditText;
    @BindView(R.id.simulator_monthly_payment_tv) TextView mMonthlyPayment;
    @BindView(R.id.simulator_total_payment_tv) TextView mTotalPayment;
    @BindView(R.id.simulator_reset_fab) FloatingActionButton mResetFab;

    private int mLoanAmount;
    private int mFinancialContribution;
    private float mAmountOfInterestPerYears;
    private float mAmountOfInterestFromAmountValues;
    private int mNbrOfYears;

    //CONSTRUCTOR
    public LoanSimulatorFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_loan_simulator, container, false);
        // Inflate the layout for this fragment
        ButterKnife.bind(this,view);
        setDefaultValues();
        setUpSeekBar();
        setUpLoanAmountEditText();
        setUpFinancialContributionEditText();
        mResetFab.setOnClickListener(this);
        return view;
    }

    /**
     * set default value of variables
     */
    private void setDefaultValues() {
        mLoanAmount = 0;
        mLoanAmountEditText.setText("");
        mFinancialContribution = 0;
        mFinancialContributionEditText.setText("");
        mSeekBar.setProgress(0);
        mNbrOfYears = 0;
        mAmountOfInterestFromAmountValues = 0;
        mAmountOfInterestPerYears = 0;
        totalLoanAmount();
    }

    /**
     * Update field in view with all values
     */
    private void totalLoanAmount() {
        mDisplayNbrOfYears.setText(String.valueOf(mNbrOfYears)+ " year(s)");
        float mTotalPercentOfInterest = mAmountOfInterestFromAmountValues + mAmountOfInterestPerYears;
        mDisplayRateFees.setText(String.format("%.2f", mTotalPercentOfInterest) + " %");
        float totalAmountOfInterest = mLoanAmount * mTotalPercentOfInterest / 100;
        float totalAmountWithFees = mLoanAmount + totalAmountOfInterest;
        mTotalPayment.setText((String.format("%,.2f",totalAmountWithFees) + " $"));
        if (mNbrOfYears > 0){
            float totalPerMonth = totalAmountWithFees / mNbrOfYears / 12;
            mMonthlyPayment.setText(String.format("%,.2f",totalPerMonth) + " $");
        }
    }

    /**
     * Get amount of loan on user typing
     */
    private void setUpLoanAmountEditText() {
        mLoanAmountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try
                {
                    mLoanAmount = Integer.parseInt(String.valueOf(s));
                    calculateInterestFromAmount();
                }
                catch(NumberFormatException nfe) {
                    return;
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * Get amount of financial contribution on user typing
     */
    private void setUpFinancialContributionEditText() {
        mFinancialContributionEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try
                {
                    mFinancialContribution = Integer.parseInt(String.valueOf(s));
                    calculateInterestFromAmount();
                }
                catch(NumberFormatException nfe) {
                    return;
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    /**
     * Manage number of years
     */
    private void setUpSeekBar() {
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mNbrOfYears = progress + 1;
                calculateInterestWhenChangeNbrOfYears();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
    }

    /**
     * calculate the interest fees by years
     */
    private void calculateInterestWhenChangeNbrOfYears(){
            mAmountOfInterestPerYears = (float) (0.27 * mNbrOfYears);
            totalLoanAmount();
    }

    /**
     * Calculate percent of financial amount of customer
     * then calculate the interest fees from this percent
     */
    private void calculateInterestFromAmount(){
        int percentOfAmount = (100 * mFinancialContribution) / mLoanAmount;
        if (percentOfAmount <= 10){
            mAmountOfInterestFromAmountValues = (float) 9;
        }else if(percentOfAmount > 10 && percentOfAmount <= 30){
            mAmountOfInterestFromAmountValues = (float) 7.10;
        }else if(percentOfAmount > 30 && percentOfAmount <= 50){
            mAmountOfInterestFromAmountValues = (float) 6.50;
        }else if(percentOfAmount > 50 && percentOfAmount <= 70){
            mAmountOfInterestFromAmountValues = (float) 4.25;
        }else if(percentOfAmount > 70 && percentOfAmount <= 90){
            mAmountOfInterestFromAmountValues = (float) 2.75;
        }else {
            mAmountOfInterestFromAmountValues = (float) 1.25;
        }
        totalLoanAmount();
    }

    @Override
    public void onClick(View v) {
        setDefaultValues();
    }
}
