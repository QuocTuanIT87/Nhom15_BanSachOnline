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
import com.tuan1611pupu.appbansach.model.Category;

public class CategoryDialogFragment extends DialogFragment {
    public interface DialogListener {
        void onDialogPositiveClick(String data);
        void onDialogNegativeClick();
    }

    private CategoryDialogFragment.DialogListener mListener;
    private Category category;

    public interface OnCategoryEditedListener {
        void onCategoryEdited(Category category);
    }

    private CategoryDialogFragment.OnCategoryEditedListener onCategoryEditedListener;

    public void setOnCategoryEditedListener(CategoryDialogFragment.OnCategoryEditedListener listener) {
        this.onCategoryEditedListener = listener;
    }

    public static CategoryDialogFragment newInstance(Category category) {
        CategoryDialogFragment fragment = new CategoryDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable("category", category);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (CategoryDialogFragment.DialogListener) getTargetFragment();
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
        View view = inflater.inflate(R.layout.dialog_add_category, null);
        final EditText inputEditText = view.findViewById(R.id.input_txt_categoryName);
        if (getArguments() != null) {
            category = (Category) getArguments().getSerializable("category");
            // Sử dụng đối tượng Author ở đây
            inputEditText.setText(category.getCategoryName());
        }
        builder.setView(view)
                .setTitle("Thông tin the loai")
                .setPositiveButton("Luu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // Trả lại thông tin tác giả đã được sửa đổi
                        if (category != null) {
                            String categoryName = inputEditText.getText().toString();
                            category.setCategoryName(categoryName);
                            if (onCategoryEditedListener != null) {
                                onCategoryEditedListener.onCategoryEdited(category);
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
