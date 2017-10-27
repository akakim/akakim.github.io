package tripath.com.samplekapp

import android.os.Build

/**
 * Created by SSLAB on 2017-07-26.
 */

class StaticValues{
    companion  object {

        const val basicURL = "http://qsales.autoground.tripath.work"
        const val SHOW_DIALOG_MESSAGE = 100
        const val HIDE_DIALOG_MESSAGE = 101


        const val SHOW_DILATING_DIALOG_MESSAGE = 101;
        const val REMOVE_DILATING_DIALOG_MESSAGE = 102;


        const val JAVA_SCRIPT_CALLBACK = 200
        const val JAVA_SCRIPT_BACK_CALLBACK = 200
        const val ON_PAGE_FINISHED = 201


        const val SPLASHE_ANIMATION_MESSAGE = 1

        const val SHARED_FCM_TOKEN = "FCM_TOKEN"

        const val ADVISOR_SEQUENCE = "advisorSeq"
        const val CHATROOM_AUTH_CODE = "chatroomAuthorizationCode"
        const val MESSAGE = "message"

        const val USER_CACHE = "userCache"
        const val SYSTEM_CACHE = "systemCache"
        const val AUTO_LOGIN = "autoLogin"
        const val ID = "id"
        const val PASSWORD = "password"
        const val FCM_TOKEN = "fcm_token"


        const val ITEM_LIST_FRAGMENT = "ItemListFragment"
        const val SYSTEM_SETTING = 999



        const val REGISTRATION_READY = "registrationReady"
        const val REGISTRATION_GENERATING = "registrationGenerating"
        const val REGISTRATION_COMPLETE = "registrationComplete"
        const val REFRESH_CHATROOMS = "refreshChatRooms"

        var IS_LOLLIPOP = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP }

}
//    const val SHOW_DIALOG_MESSAGE = 1;
