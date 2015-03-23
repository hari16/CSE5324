package com.hari.autotasx;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by Hari on 27-Feb-15.
 */
public class GeoFence {

    private float radius;
    private String name;
    private LatLng point;

    public GeoFence() {
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LatLng getPoint() {
        return point;
    }

    public void setPoint(LatLng point) {
        this.point = point;
    }

    @Override
    public String toString() {
        return "GeoFence{" +
                "radius=" + radius +
                ", name='" + name + '\'' +
                ", point=" + point +
                '}';
    }
}
