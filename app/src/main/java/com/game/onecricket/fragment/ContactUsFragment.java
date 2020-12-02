package com.game.onecricket.fragment;

import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.game.onecricket.R;

public class ContactUsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_us, container, false);

        TextView mailTV = (TextView) view.findViewById(R.id.email);
        mailTV.setText(Html.fromHtml("<a href=\"mailto:"+getString(R.string.emailid)+"?subject="+getString(R.string.email_subject)+"\" >"+getString(R.string.emailid)+"</a>"));
        mailTV.setMovementMethod(LinkMovementMethod.getInstance());

        Spanned policy = Html.fromHtml(getString(R.string.messageWithLink));
        TextView termsOfUse = (TextView)view.findViewById(R.id.fags);
        termsOfUse.setText(policy);
        termsOfUse.setMovementMethod(LinkMovementMethod.getInstance());

        return view;
    }
}
