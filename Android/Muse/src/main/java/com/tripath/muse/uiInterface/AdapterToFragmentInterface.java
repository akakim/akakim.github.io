/*
 *  AdapterToFragmentInterface.java 1.0.0 2017/10/23
 *
 * Copyright (c) 2017 RyoRyeong KIM
 * All rights reserved
 *
 * this software is the confidential and proprietary information to Ryo Ryeong KIM
 * you shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with RyoRyeong KiM
 */
package com.tripath.muse.uiInterface;


/**
 * 어뎁터의 이벤트를 fragment로 전달하는 interface
 *
 * @version  1.0.0 23 Oct 2017
 * @author Kim RyoRyeong
 */
public interface AdapterToFragmentInterface {
    void onItemClicked(int position);
}
