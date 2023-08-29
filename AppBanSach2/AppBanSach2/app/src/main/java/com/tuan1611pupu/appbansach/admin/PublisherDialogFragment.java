package com.tuan1611pupu.appbansach.admin;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.tuan1611pupu.appbansach.R;
import com.tuan1611pupu.appbansach.model.Author;
import com.tuan1611pupu.appbansach.model.Publisher;

public class PublisherDialogFragment extends DialogFragment {
    public interface DialogListener {
        void onDialogPositiveClick(String data);
        void onDialogNegativeClick();
    }

    private PublisherDialogFragment.DialogListener mListener;
    private Publisher publisher;

    public interface OnPublisherEditedListener {
        void onPublisherEdited(Publisher publisher);
    }

    private PublisherDialogFragment.OnPublisherEditedListener onPublisherEditedListener;

    public void setOnPublisherEditedListener(PublisherDialogFragment.OnPublisherEditedListener listener) {
        this.onPublisherEditedListener = listener;
    }

    public static PublisherDialogFragment newInstance(Publisher publisher) {
        PublisherDialogFragment fragment = new PublisherDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("publisher", publisher);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (PublisherDialogFragment.DialogListener) getTargetFragment();
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
        View view = inflater.inflate(R.layout.dialog_add_publisher, null);
        final EditText inputEditText = view.findViewById(R.id.input_txt_publisherName);
        if (getArguments() != null) {
            publisher = (Publisher) getArguments().getSerializable("publisher");
            // Sử dụng đối tượng Author ở đây
            inputEditText.setText(publisher.getPublisherName());
        }

        builder.setView(view)
                .setTitle("Thông tin nhà xuất bản ")
                .setPositiveButton("Luu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Trả lại thông tin tác giả đã được sửa đổi
                        if (publisher != null) {
                            String publisherName = inputEditText.getText().toString();
                            publisher.setPublisherName(publisherName);
                            if (onPublisherEditedListener != null) {
                                onPublisherEditedListener.onPublisherEdited(publisher);
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
