#!/bin/bash

# Configuration
API_URL="http://localhost:8080/api/transactions"
ADMIN_AUTH="admin:admin123"

echo "--------------------------------------------------"
echo "🚀 Seeding Sample Financial Data to Zorvyn Solution"
echo "--------------------------------------------------"

# Function to post a transaction
post_transaction() {
  local amount=$1
  local type=$2
  local category=$3
  local notes=$4
  local date=$5

  echo "Adding $type: $category ($amount)..."
  
  curl -s -u "$ADMIN_AUTH" -X POST "$API_URL" \
    -H "Content-Type: application/json" \
    -d "{
      \"amount\": $amount,
      \"type\": \"$type\",
      \"category\": \"$category\",
      \"notes\": \"$notes\",
      \"date\": \"$date\"
    }" > /dev/null
}

# Current Date Components
YEAR=$(date +%Y)
MONTH=$(date +%m)

# Seed Data (Current and Past Month)
post_transaction 150.00 "EXPENSE" "Internet" "Fiber optic bill" "$YEAR-$MONTH-05"
post_transaction 85.20 "EXPENSE" "Groceries" "Weekly grocery run" "$YEAR-$MONTH-10"
post_transaction 2000.00 "INCOME" "Salary" "Freelance project payment" "$YEAR-$MONTH-15"
post_transaction 45.00 "EXPENSE" "Subscription" "Netflix & Spotify" "$YEAR-$MONTH-12"
post_transaction 300.00 "EXPENSE" "Utility" "Gas & Electric" "$YEAR-$MONTH-18"

# More data for trend analysis
post_transaction 1200.00 "EXPENSE" "Housing" "Rent payment" "$YEAR-03-01"
post_transaction 5000.00 "INCOME" "Salary" "March salary" "$YEAR-03-30"

echo "--------------------------------------------------"
echo "✅ Data Seeding Completed!"
echo "--------------------------------------------------"
