package com.tuan1611pupu.appbansach.admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.tuan1611pupu.appbansach.R;
import com.tuan1611pupu.appbansach.model.Author;

public class AuthorDialogFragment extends DialogFragment {
    public interface DialogListener {
        void onDialogPositiveClick(String data);

        void onDialogNegativeClick();
    }

    private DialogListener mListener;
    private Author author;

    public interface OnAuthorEditedListener {
        void onAuthorEdited(Author author);
    }

    private OnAuthorEditedListener onAuthorEditedListener;

    public void setOnAuthorEditedListener(OnAuthorEditedListener listener) {
        this.onAuthorEditedListener = listener;
    }

    public static AuthorDialogFragment newInstance(Author author) {
        AuthorDialogFragment fragment = new AuthorDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("author", author);
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (DialogListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling Fragment must implement DialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        // Tạo giao diện cho DialogFragment
        View view = inflater.inflate(R.layout.dialog_add_author, null);
        final EditText inputEditText = view.findViewById(R.id.input_txt_authorName);
        if (getArguments() != null) {
            author = (Author) getArguments().getSerializable("author");
            // Sử dụng đối tượng Author ở đây
            inputEditText.setText(author.getAuthorName());
        }

        builder.setView(view)
                .setTitle("Thông tin tác giả")
                .setPositiveButton("Luu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Trả lại thông tin tác giả đã được sửa đổi
                        if (author != null) {
                            String authorName = inputEditText.getText().toString();
                            author.setAuthorName(authorName);
                            if (onAuthorEditedListener != null) {
                                onAuthorEditedListener.onAuthorEdited(author);
                            }
                        } else {
                            String data = inputEditText.getText().toString();
                            mListener.onDialogPositiveClick(data);
                        }

                    }
                })
                .setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Không làm gì cả khi người dùng bấm nút "Hủy"
                        if (mListener != null) {
                            if (id == DialogInterface.BUTTON_POSITIVE) {
                                String data = inputEditText.getText().toString();
                                mListener.onDialogPositiveClick(data);
                            } else if (id == DialogInterface.BUTTON_NEGATIVE) {
                                mListener.onDialogNegativeClick();
                            }
                        }
                    }
                });

        return builder.create();

    }
}

