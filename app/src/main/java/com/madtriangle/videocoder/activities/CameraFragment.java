package com.madtriangle.videocoder.activities;

import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.madtriangle.videocoder.R;
import com.madtriangle.videocoder.views.CameraTextureView;

public class CameraFragment extends Fragment {

    private FrameLayout cameraFrame;
    private CameraTextureView cameraTextureView;
    private int numOfCameras;
    private int frontCameraId = Integer.MAX_VALUE;
    private int backCameraId = Integer.MAX_VALUE;
    private boolean isFacingBack = true;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public static CameraFragment newInstance(String param1, String param2) {
        CameraFragment fragment = new CameraFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public CameraFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        numOfCameras = Camera.getNumberOfCameras();
        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();

        for (int i = 0; i < numOfCameras; i++) {
            Camera.getCameraInfo(i, cameraInfo);
            if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {
                backCameraId = i;
            } else if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
                frontCameraId = i;
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_camera, container, false);
        cameraFrame = (FrameLayout) rootView.findViewById(R.id.cameraFrame);
        cameraTextureView = new CameraTextureView(getActivity().getApplicationContext(),getCameraInstance(frontCameraId));
        cameraFrame.addView(cameraTextureView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (cameraTextureView.getCamera() == null) {
            cameraTextureView.setCamera(getCameraInstance(frontCameraId));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        cameraTextureView.getCamera().release();
        cameraTextureView.setCamera(null);
    }

    public Camera getCameraInstance(int cameraId){
        Camera c = null;
        try {
            c = Camera.open(cameraId); // attempt to get a Camera instance
        }
        catch (Exception e){
            // Camera is not available (in use or does not exist)
        }
        return c; // returns null if camera is unavailable
    }

}
