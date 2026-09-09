package com.example.project_1_part_3.model;
/**
 * Interface for game objects that scale based on window dimensions.
 */
public interface Resizable {
    /** Adjusts horizontal position or width by a scale factor. */
    void resizeX(double factor);
    /** Adjusts vertical position or height by a scale factor. */
    void resizeY(double factor);
}
