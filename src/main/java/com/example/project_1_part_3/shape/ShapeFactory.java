package com.example.project_1_part_3.shape;
/**
 * Factory class to instantiate Shape objects by type.
 */
public class ShapeFactory {
    /** * Returns a new Shape based on the provided string.
     * @throws IllegalArgumentException if type is unrecognized.
     */
    public static Shape createShape(String type){
        return switch(type.toLowerCase()){
            case "circle" -> new CircleShape();
            case "rectangle" -> new RectangleShape();
            default -> throw new IllegalArgumentException("Unknown Type");
        };
    }
}
