package hr.fitme;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import hr.fitme.models.JwtAuthenticationResponse;
import hr.fitme.models.User;

public class RegisterActivity extends AppCompatActivity {

    private User user;
    private UserRegisterTask registerTask;

    private EditText emailView;
    private EditText usernameView;
    private EditText passwordView;
    private EditText repeatPasswordView;
    private EditText dateOfBirthView;
    private EditText weightPicker;
    private EditText heightPicker;
    private Button signUp;
    private Button cancel;
    private RadioButton femaleButton;
    private RadioButton maleButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        emailView = (EditText) findViewById(R.id.sign_up_email);
        usernameView = (EditText) findViewById(R.id.username);
        passwordView = (EditText) findViewById(R.id.sign_up_password);
        repeatPasswordView = (EditText) findViewById(R.id.repeat_password);
        dateOfBirthView = (EditText) findViewById(R.id.date_of_birth);
        weightPicker = (EditText) findViewById(R.id.weight_picker);
        heightPicker = (EditText) findViewById(R.id.height_picker);
        signUp = (Button) findViewById(R.id.sign_in_button);
        cancel = (Button) findViewById(R.id.cancel_button);
        femaleButton = (RadioButton) findViewById(R.id.female_button);
        maleButton = (RadioButton) findViewById(R.id.male_button);

        dateOfBirthView.addTextChangedListener(new DateInputTextWatcher(dateOfBirthView));

        user = new User();

        
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), LoginActivity.class));
            }
        });

        femaleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    maleButton.setChecked(false);
                    user.setGender(true);
                }
//                else {
//                    maleButton.setChecked(true);
//                    user.setGender(true);
//                }
            }
        });
        maleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    femaleButton.setChecked(false);
                    user.setGender(false);
                }
//                else {
//                    femaleButton.setChecked(true);
//                    user.setGender(false);
//                }
            }
        });

        
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptSignUp();
            }
        });


    }

    private void attemptSignUp() {
        if (registerTask != null) {
            return;
        }

        // Reset errors.
        emailView.setError(null);
        usernameView.setError(null);
        passwordView.setError(null);
        repeatPasswordView.setError(null);
        heightPicker.setError(null);
        weightPicker.setError(null);

        // Store values at the time of the login attempt.
        user.setUsername(usernameView.getText().toString());
        user.setEmail(emailView.getText().toString());
        user.setPassword(passwordView.getText().toString());
        user.setHeight(new Float(heightPicker.getText().toString()));
        user.setDesiredWeight(new Float(weightPicker.getText().toString()));
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        try {
            Date date = (Date) format.parse(dateOfBirthView.getText().toString());
            user.setDateOfBirth(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }



        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (TextUtils.isEmpty(passwordView.getText().toString())
                || !isPasswordValid(passwordView.getText().toString(), repeatPasswordView.getText().toString())) {
            passwordView.setError(getString(R.string.invalid_password));
            focusView = passwordView;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(emailView.getText().toString())) {
            emailView.setError(getString(R.string.error_field_required));
            focusView = emailView;
            cancel = true;
        } else if (!isEmailValid(emailView.getText().toString())) {
            emailView.setError(getString(R.string.error_invalid_email));
            focusView = emailView;
            cancel = true;
        }

        //Check for valid username.
        if (TextUtils.isEmpty(usernameView.getText().toString())) {
            usernameView.setError(getString(R.string.error_field_required));
            focusView = usernameView;
            cancel = true;
        }

        if (TextUtils.isEmpty(heightPicker.getText().toString())) {
            heightPicker.setError(getString(R.string.error_field_required));
            focusView = heightPicker;
            cancel = true;
        }

        if (TextUtils.isEmpty(weightPicker.getText().toString())) {
            weightPicker.setError(getString(R.string.error_field_required));
            focusView = weightPicker;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            registerTask = new UserRegisterTask(user);
            registerTask.execute((Void) null);
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        final View mLoginFormView = findViewById(R.id.login_form);
        final View mProgressView = findViewById(R.id.login_progress);

        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    private boolean isEmailValid(String email) {
        return email.contains("@");
    }

    private boolean isPasswordValid(String password, String repeatPass) {
        return (password.length() > 4 && password.equals(repeatPass));
    }

    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private User user;

        UserRegisterTask(User user) {
            this.user = user;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                final String url = getResources().getString(R.string.rest_url) + "/auth/register";
                RestTemplate restTemplate = new RestTemplate();
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON);
                restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

                HttpEntity<User> request = new HttpEntity<>(user, headers);
                JwtAuthenticationResponse responseEntity = restTemplate.postForObject(url, request, JwtAuthenticationResponse.class);

                FitmeApplication.token = responseEntity.getToken();

                // We need an Editor object to make preference changes.
                // All objects are from android.context.Context
                SharedPreferences loggedIn = getSharedPreferences(getResources().getString(R.string.PREFERENCES), 0);
                SharedPreferences.Editor editor = loggedIn.edit();
                editor.putBoolean(getResources().getString(R.string.LOGGEDINPREFERENCE), true);
                editor.commit();

                SharedPreferences token = getSharedPreferences(getResources().getString(R.string.PREFERENCES), 0);
                SharedPreferences.Editor tokenEditor = token.edit();
                tokenEditor.putString(getString(R.string.TOKENPREFERENCE), FitmeApplication.token);
                tokenEditor.commit();

                return true;
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            registerTask = null;
            showProgress(false);

            if (success) {
                finish();
                startActivity(new Intent(RegisterActivity.this, MainActivity.class));
            } else {
                Toast.makeText(RegisterActivity.this, "User is not registered.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        protected void onCancelled() {
            registerTask = null;
            showProgress(false);
        }
    }


    public class DateInputTextWatcher implements TextWatcher {

        public static final String DATE_SEPARATOR = "/";

        private DateFormatter dateFormatter;
        private EditText editText;
        private String prevText;

        public DateInputTextWatcher(EditText editText) {
            this.editText = editText;
            dateFormatter = new DateFormatter();
        }

        @Override
        public void afterTextChanged(Editable text) {
            int prevLength = prevText == null ? 0 : prevText.length();
            int newLength = text == null ? 0 : text.length();
            boolean deleted = newLength < prevLength;

            // Don't try to format if the user is deleting text. Auto-fixing the input
            // date while the user is already trying to possibly fix a mistake would
            // be terrible.
            if (!deleted && text != null) {
                int cursorPosition = editText.getSelectionStart();
                boolean shouldAddMissingZeroes = cursorPosition == text.length();
                String parsedDate = dateFormatter.parse(text.toString(), shouldAddMissingZeroes);

                // Remove all listeners on the field (which includes DateInputTextWatcher)
                // before modifying the text to avoid going to infinite loop.
                editText.removeTextChangedListener(this);
                text.replace(0, text.length(), parsedDate);
                editText.addTextChangedListener(this);
            }

            prevText = text == null ? null : text.toString();
        }

        /**
         * This might not be the best formatter, but I had fun implementing this. Instead of working on the date-to-format
         * as a single entity, it considers it as a combination of three separate parts: DD, MM and YYYY.
         */
        private class DateFormatter {

            private static final char DATE_SEPARATOR_CHAR = '/';
            private static final int MAX_BATCHES = 3;
            private static final int MAX_LENGTH_YEAR = 4;

            private String fullText = "";
            private String currentPart = "";
            private int partCount = 0;
            private boolean canAddMissingZeroes;

            /**
             * @param dateString          The date to format.
             * @param canAddMissingZeroes When enabled, a 0 will automatically get added if the user fills a single digit in
             *                            day or month field. That is, 12/4/____ will become 12/04/____. It's recommended to
             *                            enable this only when the user is appending characters at the end. That is, the
             *                            cursor is at the end or be prepared to see strange auto-formatting.
             */
            public String parse(String dateString, boolean canAddMissingZeroes) {
                this.canAddMissingZeroes = canAddMissingZeroes;
                resetState();

                // Construct the formatted date
                for (char aChar : dateString.toCharArray()) {
                    addNextChar(aChar);
                }
                endConstruction();

                return fullText;
            }

            private DateFormatter addNextChar(char nextChar) {
                // Plan:
                // YYYY: Let the year digits pass till 4 digits
                // DD and MM:
                //  1. When "/" is encountered -> add a leading 0 if required -> dispatch this batch
                //  2. When length(batch) == 2 digits -> add "\" -> dispatch this batch

                if (partCount == MAX_BATCHES - 1) {
                    // This is the YYYY part. No formatting is required here.
                    if (currentPart.length() >= MAX_LENGTH_YEAR || nextChar == DATE_SEPARATOR_CHAR) {
                        // The date is complete. The user is probably entering more than 4 digits
                        // for the year. Ignore.
                        return this;
                    }
                    currentPart += nextChar;
                    fullText += nextChar;

                } else {
                    // Inside either DD or MM part
                    if (nextChar == DATE_SEPARATOR_CHAR) {
                        // Don't accept successive '/'s
                        if (currentPart.length() == 0 || currentPart.contains(DATE_SEPARATOR)) {
                            return this;
                        }

                        // Add a leading 0 if required + allowed
                        if (canAddMissingZeroes && currentPart.length() == 1) {
                            currentPart = "0" + currentPart;
                        }
                    }

                    // Append this new char
                    currentPart += nextChar;

                    // And a slash if the user has already entered 2 digits. At this point, a
                    // 2-digit-strong part will not already have any slashes.
                    if (currentPart.length() == 2 && !currentPart.contains(DATE_SEPARATOR)) {
                        currentPart += DATE_SEPARATOR_CHAR;
                    }

                    // Mark this part as complete if possible
                    if (currentPart.length() == 3) {
                        fullText += currentPart;
                        currentPart = "";
                        partCount++;
                    }
                }

                return this;
            }

            void endConstruction() {
                // Append the left-over part
                if (partCount != MAX_BATCHES - 1) {
                    fullText += currentPart;
                }
            }

            void resetState() {
                partCount = 0;
                fullText = "";
                currentPart = "";
            }

        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

    }

}
