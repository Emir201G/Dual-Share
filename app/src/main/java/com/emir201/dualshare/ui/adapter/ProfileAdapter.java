package com.emir201.dualshare.ui.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.emir201.dualshare.R;
import com.emir201.dualshare.data.local.entity.UserEntity;
import com.emir201.dualshare.model.ProfileItem;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final List<ProfileItem> items;
    private final OnItemClickListener listener;
    private UserEntity user;

    public interface OnItemClickListener {
        void onOptionClick(String optionName);
    }

    public ProfileAdapter(List<ProfileItem> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    public void updateUserData(UserEntity user) {
        this.user = user;
        notifyItemChanged(0);
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        switch (viewType) {
            case ProfileItem.TYPE_HEADER:
                return new HeaderViewHolder(inflater.inflate(R.layout.item_profile_header, parent, false));
            case ProfileItem.TYPE_TITLE:
                return new TitleViewHolder(inflater.inflate(R.layout.item_section_title, parent, false));
            case ProfileItem.TYPE_FOOTER:
                return new FooterViewHolder(inflater.inflate(R.layout.item_profile_footer, parent, false));
            default:
                return new OptionViewHolder(inflater.inflate(R.layout.item_profile_option, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ProfileItem item = items.get(position);

        if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder h = (HeaderViewHolder) holder;

            if (user != null) {
                h.txtNameUser.setText(user.getUsername());
                h.txtGmail.setText(user.getEmail());

                if (user.getPhotoUrl() != null && !user.getPhotoUrl().isEmpty()) {
                    Glide.with(h.itemView.getContext())
                            .load(user.getPhotoUrl())
                            .into(h.imgProfile);
                } else {
                    h.imgProfile.setImageResource(R.drawable.profile);
                }
            }

        } else if (holder instanceof TitleViewHolder) {
            ((TitleViewHolder) holder).txtSectionTitle.setText(item.getText());
        } else if (holder instanceof OptionViewHolder) {
            OptionViewHolder o = (OptionViewHolder) holder;
            o.txtOptionName.setText(item.getText());

            if (item.getIconRes() != 0) {
                o.imgOptionIcon.setImageResource(item.getIconRes());
                o.imgOptionIcon.setVisibility(View.VISIBLE);
            } else {
                o.imgOptionIcon.setVisibility(View.GONE);
            }

            applyTouchAnimation(o.itemView);

            o.itemView.setOnClickListener(v -> listener.onOptionClick(item.getText()));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        CircleImageView imgProfile;
        TextView txtNameUser, txtGmail;


        HeaderViewHolder(View v) {
            super(v);
            imgProfile = v.findViewById(R.id.imgProfile);
            txtNameUser = v.findViewById(R.id.txtNameUser);
            txtGmail = v.findViewById(R.id.txtGmail);
        }
    }

    static class TitleViewHolder extends RecyclerView.ViewHolder {
        TextView txtSectionTitle;

        TitleViewHolder(View v) {
            super(v);
            txtSectionTitle = v.findViewById(R.id.txtSectionTitle);
        }
    }

    static class OptionViewHolder extends RecyclerView.ViewHolder {
        TextView txtOptionName;
        ImageView imgOptionIcon;

        OptionViewHolder(View v) {
            super(v);
            txtOptionName = v.findViewById(R.id.txtOptionName);
            imgOptionIcon = v.findViewById(R.id.imgOptionIcon);
        }
    }

    static class FooterViewHolder extends RecyclerView.ViewHolder {
        FooterViewHolder(View v) {
            super(v);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void applyTouchAnimation(View view) {
        view.setOnTouchListener((v, event) -> {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    view.animate().scaleX(0.90f).scaleY(0.90f).setDuration(80).start();
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    view.animate().scaleX(1f).scaleY(1f).setDuration(80).start();
                    break;
            }
            return false;
        });
    }
}