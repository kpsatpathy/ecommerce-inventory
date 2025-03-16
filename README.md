# 🛒 E-Commerce Management System

## 🔍 Overview
The **E-Commerce Management System** is a ☕ Java-based application built using **Java Swing 🎨** for the GUI and **🐬 MySQL** for database management. This system provides functionalities for ➕ adding, 🔄 managing, and 👀 viewing products in an 📦 inventory, making it suitable for small businesses and startups.

## ✨ Features
- **🖥️ User-Friendly Interface:** Built with Java Swing for an intuitive experience.
- **📦 Product Management:** ➕ Add, ✏️ update, and ❌ delete products with ease.
- **📊 Inventory Tracking:** 👀 View and 📋 manage stock levels efficiently.
- **🔍 Search & Filter:** 🔎 Find products by 🏷️ name, 🗂️ category, or 🔢 ID.
- **💰 Automated Price Calculation:** Calculates 💲 prices including GST (18%).
- **📤 Export Data:** 📄 Save inventory details as a 🗂️ CSV file.
- **💾 Secure Database Integration:** Uses 🐬 MySQL for encrypted data storage.

## 🛠️ Technologies Used
- **☕ Java (Swing)** – GUI 🎨 development
- **🐬 MySQL** – Database 🗄️ for structured data storage
- **🔌 JDBC** – Database connectivity layer
- **🌟 Nimbus Look and Feel** – Modern UI styling

## 📌 Setup Instructions
### 📋 Prerequisites
Ensure you have the following installed:
- ☕ Java JDK 8 or later
- 🐬 MySQL Server
- 🔌 MySQL Connector/J (JDBC Driver)

### 🗄️ Database Setup
1. Create a MySQL database:
    ```sql
    CREATE DATABASE inventory_db;
    ```
2. Switch to the new database:
    ```sql
    USE inventory_db;
    ```
3. Create the `products` table:
    ```sql
    CREATE TABLE products (
        id INT AUTO_INCREMENT PRIMARY KEY,
        name VARCHAR(255) NOT NULL,
        price DOUBLE NOT NULL,
        quantity INT NOT NULL,
        category VARCHAR(100) NOT NULL
    );
    ```

### 🚀 Running the Application
1. Clone the repository:
    ```sh
    git clone https://github.com/kpsatpathy/ECommerce-Management-System.git
    cd ECommerce-Management-System
    ```
2. Update database credentials in `DBConnector.java`:
    ```java
    private static final String URL = "jdbc:mysql://localhost:3306/inventory_db";
    private static final String USER = "your_db_user";
    private static final String PASSWORD = "your_db_password";
    ```
3. Compile and run the program:
    ```sh
    javac MainGUI.java
    java MainGUI
    ```

## 📁 Project Structure
```
ECommerce-Management-System/
│── 📂 src/
│   ├── 🗄️ DBConnector.java  # Database connection logic
│   ├── 🖥️ MainGUI.java      # Main user interface logic
```

## 🔮 Future Enhancements
- **🔐 User Authentication** – Implement role-based access control.
- **📦 Order Management** – Enable processing and tracking of 📦 orders.
- **📊 Reports & Analytics** – Generate 📈 sales reports and data insights.

## 📜 License
This project is licensed under the ⚖️ MIT License.

## 🤝 Contributors
- **Deepam** ([@Link](https://github.com/PHONEIX-06))
- **Tanya** ([@Link](https://github.com/TANYA2405))

## 📧 Contact
For queries, reach out at 📩 [kaibalyaprasad3@gmail.com](mailto:kaibalyaprasad3@gmail.com).

