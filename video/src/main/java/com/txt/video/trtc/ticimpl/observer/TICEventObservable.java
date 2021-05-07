package com.txt.video.trtc.ticimpl.observer;

import com.txt.video.trtc.ticimpl.TICEventListener;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TICEventObservable extends  TICObservable<TICEventListener> implements TICEventListener {

    @Override
    public void onTICVideoDisconnect(int i, String s) {
        LinkedList<WeakReference<TICEventListener>> tmpList = new LinkedList<>(listObservers);
        Iterator<WeakReference<TICEventListener>> it = tmpList.iterator();

        while(it.hasNext())
        {
            TICEventListener t = it.next().get();
            if (t != null) {
                t.onTICVideoDisconnect(i, s);
            }
        }
    }
    @Override
    public void onTICUserVideoAvailable(final String userId, boolean available) {

        LinkedList<WeakReference<TICEventListener>> tmpList = new LinkedList<>(listObservers);
        Iterator<WeakReference<TICEventListener>> it = tmpList.iterator();

        while(it.hasNext())
        {
            TICEventListener t = it.next().get();
            if (t != null) {
                t.onTICUserVideoAvailable(userId, available);
            }
        }
    }

    @Override
    public void onTICUserSubStreamAvailable(final String userId, boolean available) {
        LinkedList<WeakReference<TICEventListener>> tmpList = new LinkedList<>(listObservers);
        Iterator<WeakReference<TICEventListener>> it = tmpList.iterator();

        while(it.hasNext())
        {
            TICEventListener t = it.next().get();
            if (t != null) {
                t.onTICUserSubStreamAvailable(userId, available);
            }
        }
    }

    @Override
    public void onTICUserAudioAvailable(final String userId, boolean available) {
        LinkedList<WeakReference<TICEventListener>> tmpList = new LinkedList<>(listObservers);
        Iterator<WeakReference<TICEventListener>> it = tmpList.iterator();

        while(it.hasNext())
        {
            TICEventListener t = it.next().get();
            if (t != null) {
                t.onTICUserAudioAvailable(userId, available);
            }
        }
    }

    @Override
    public void onTICClassroomDestroy() {

        LinkedList<WeakReference<TICEventListener>> tmpList = new LinkedList<>(listObservers);
        Iterator<WeakReference<TICEventListener>> it = tmpList.iterator();

        while(it.hasNext())
        {
            TICEventListener t = it.next().get();
            if (t != null) {
                t.onTICClassroomDestroy();
            }
        }
    }

    @Override
    public void onTICMemberJoin(List<String> list) {

        LinkedList<WeakReference<TICEventListener>> tmpList = new LinkedList<>(listObservers);
        Iterator<WeakReference<TICEventListener>> it = tmpList.iterator();

        while(it.hasNext())
        {
            TICEventListener t = it.next().get();
            if (t != null) {
                t.onTICMemberJoin(list);
            }
        }
    }

    @Override
    public void onTICMemberQuit(List<String> list) {

        LinkedList<WeakReference<TICEventListener>> tmpList = new LinkedList<>(listObservers);
        Iterator<WeakReference<TICEventListener>> it = tmpList.iterator();

        while(it.hasNext())
        {
            TICEventListener t = it.next().get();
            if (t != null) {
                t.onTICMemberQuit(list);
            }
        }
    }

    @Override
    public void onTICSendOfflineRecordInfo(int code, String desc) {

        LinkedList<WeakReference<TICEventListener>> tmpList = new LinkedList<>(listObservers);
        Iterator<WeakReference<TICEventListener>> it = tmpList.iterator();

        while(it.hasNext())
        {
            TICEventListener t = it.next().get();
            if (t != null) {
                t.onTICSendOfflineRecordInfo(code, desc);
            }
        }
    }
}
