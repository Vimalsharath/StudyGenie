# Implementation Plan: Planner UI/UX & Theming Enhancement

Enhance the `PlannerScreen` with vibrant colors, improved hierarchy, and distinct priority styling to create a premium "StudyGenie" experience.

## Proposed Changes

### 1. Planner Screen Styling
#### [MODIFY] [PlannerScreen.kt](file:///C:/Users/sharath%20V/AndroidStudioProjects/StudyGenie/app/src/main/java/com/studygenie/app/presentation/screens/PlannerScreen.kt)
- **Gradient Header**: Add a professional gradient background behind the "Study Planner" title.
- **Input Grouping**: Wrap the input fields in an `ElevatedCard` with a slightly different background color to separate "Planning" from the "List".
- **Vibrant Priority Chips**:
    - High: Red background when selected.
    - Medium: Amber background when selected.
    - Low: Emerald/Green background when selected.
- **Dynamic Icons**: Add tinting to the Calendar and Clock icons to match the primary theme.

### 2. Task Card Premium Upgrade
#### [MODIFY] [TaskCard.kt](file:///C:/Users/sharath%20V/AndroidStudioProjects/StudyGenie/app/presentation/components/TaskCard.kt)
- **Priority Indicator**: Add a vertical colored strip on the left edge of the card (Red/Yellow/Green) to show priority at a glance.
- **Improved Spacing**: Refine padding and text styles for better readability.
- **Action Buttons**: Use `FilledTonalIconButton` for the "Complete" action to make it feel more interactive.

### 3. Theme Consistency
- Ensure all custom colors (Red/Amber/Green) use the theme's color palette where possible or consistent hex codes for branding.

---

## Verification Plan

### Manual Verification
1.  **Visual Audit**: Verify the Planner screen now has a distinct "Header" area.
2.  **Chip Feedback**: Tap each priority chip and verify the color matches (Red for High, etc.).
3.  **Card Layout**: Verify the new "Priority Strip" appears correctly on the left of each task.
4.  **Dark Mode**: Ensure the new colors have sufficient contrast in Dark Mode.
