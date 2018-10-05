/**
 * Copyright 2016 JustWayward Team
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.share.open_source.widget;

import android.graphics.Color;

import com.share.open_source.constants.Constants;

import java.util.ArrayList;
import java.util.List;


public class TagColor {

    public int borderColor = Color.parseColor("#49C120");
    public int backgroundColor = Color.parseColor("#49C120");
    public int textColor = Color.WHITE;

    public static List<TagColor> getRandomColors(int size){

        List<TagColor> list = new ArrayList<>();
        for(int i=0; i< size; i++){
            TagColor color = new TagColor();
            color.borderColor = color.backgroundColor = Constants.tagColors[i % Constants.tagColors.length];
            list.add(color);
        }
        return list;
    }
}
