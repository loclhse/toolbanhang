# New Order Flow Approach (2024-06)

## Overview
This document describes the updated approach for handling table orders in the Food Ordering System. The new flow simplifies the process for staff by removing chair selection and focusing on a streamlined experience:

1. **Table Selection:** Staff are presented with a list or grid of tables.
2. **Order Initiation:** Staff clicks on a table number to begin an order.
3. **Pop-up/Modal:** A small pop-up appears, prompting staff to:
    - Enter the number of people at the table.
    - Select food items from the menu for the order.
4. **Order Creation:** The order is created and associated with the selected table, the number of people, and the chosen food items. No seat/chair selection is required.

## Backend API Implications
- **Order Creation Endpoint:**
  - Accepts: `tableId`, `numberOfPeople`, and a list of food items.
  - Does **not** require seat/chair information.
- **OrderRequestDTO:**
  - Fields: `tableId`, `numberOfPeople`, `foodItems` (list).
- **Order Logic:**
  - The order is linked to the table and includes the number of people and selected food items.
  - Optionally, the table can be marked as "occupied" when an order is created.

## Example API Request
```json
{
  "tableId": 5,
  "numberOfPeople": 3,
  "foodItems": [
    { "foodItemId": 101, "quantity": 2 },
    { "foodItemId": 205, "quantity": 1 }
  ]
}
```

## Notes
- This approach removes all logic and UI related to individual seat/chair selection.
- The focus is on speed and simplicity for staff during busy service times.

---

