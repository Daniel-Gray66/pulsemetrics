package com.pulsemetrics.pulsemetrics.model;

public enum GoalDirection {
    ABOVE,  // value should be above goal (e.g. steps > 10000)
    BELOW   // value should be below goal (e.g. sleep < 9 hours)
}