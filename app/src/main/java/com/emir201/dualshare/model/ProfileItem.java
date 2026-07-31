

package com.emir201.dualshare.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ProfileItem {
    public static final int TYPE_HEADER = 0;
    public static final int TYPE_TITLE = 1;
    public static final int TYPE_OPTION = 2;
    public static final int TYPE_FOOTER = 3;

    private int type;
    private String text;
    private int iconRes;

    public ProfileItem(int type, String text) {
        this.type = type;
        this.text = text;
        this.iconRes = 0;
    }

}