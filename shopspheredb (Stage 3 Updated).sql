-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Apr 20, 2026 at 02:27 PM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `shopspheredb`
--

-- --------------------------------------------------------

--
-- Table structure for table `customer`
--

CREATE TABLE `customer` (
  `customer_id` int(11) NOT NULL,
  `name` varchar(100) NOT NULL,
  `email` varchar(150) NOT NULL,
  `phone` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `customer`
--

INSERT INTO `customer` (`customer_id`, `name`, `email`, `phone`) VALUES
(1, 'Hailey Rivers', 'haileyr@email.com', '0851111111'),
(2, 'Ashna Mehta', 'ashna22@email.com', '0852222222'),
(3, 'Ciara Walsh', 'ciara3@email.com', '0853333333'),
(4, 'Daniel Ryan', 'daniel4@email.com', '0854444444'),
(5, 'Emma Byrne', 'emma5@email.com', '0855555555'),
(6, 'Fiona Brown', 'fiona6@email.com', '0856666666'),
(7, 'Krish Kapoor', 'krishk@email.com', '0857777777'),
(8, 'Vaani Batra', 'vaanik@email.com', '0858888888'),
(9, 'Ian Collins', 'ian9@email.com', '0859999999'),
(10, 'Kiara De Silva', 'kiki10@email.com', '0860000000'),
(11, 'Meghana Rathnam', 'meg11@email.com', '0861111111'),
(12, 'Laura Smith', 'laura12@email.com', '0862222222'),
(13, 'Mark Quinn', 'mark13@email.com', '0863333333'),
(14, 'Niamh Reilly', 'niamh14@email.com', '0864444444'),
(15, 'Raveena Rathnam', 'ravi2315@email.com', '0865555555'),
(16, 'Paula Lynch', 'paula16@email.com', '0866666666'),
(17, 'Quinn Lynch', 'quinn17@email.com', '0867777777'),
(18, 'Rachel Flynn', 'rachel18@email.com', '0868888888'),
(19, 'Sean Moran', 'sean19@email.com', '0869999999'),
(20, 'Tara Fernando', 'tara20@email.com', '0870000000');

-- --------------------------------------------------------

--
-- Table structure for table `orderitem`
--

CREATE TABLE `orderitem` (
  `order_item_id` int(11) NOT NULL,
  `order_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `quantity` int(11) NOT NULL CHECK (`quantity` > 0),
  `unit_price_at_time` decimal(10,2) NOT NULL CHECK (`unit_price_at_time` >= 0),
  `file_name` varchar(255) DEFAULT '',
  `content_type` varchar(100) DEFAULT '',
  `file_size` int(11) DEFAULT 0,
  `file_data` mediumblob DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `orderitem`
--

INSERT INTO `orderitem` (`order_item_id`, `order_id`, `product_id`, `quantity`, `unit_price_at_time`, `file_name`, `content_type`, `file_size`, `file_data`) VALUES
(1, 1, 1, 1, 999.99, '', '', 0, NULL),
(2, 2, 2, 1, 699.99, '', '', 0, NULL),
(3, 3, 3, 2, 149.99, '', '', 0, NULL),
(4, 4, 4, 1, 79.99, '', '', 0, NULL),
(5, 5, 5, 3, 49.99, '', '', 0, NULL),
(6, 6, 6, 1, 249.99, '', '', 0, NULL),
(7, 7, 7, 1, 399.99, '', '', 0, NULL),
(8, 8, 8, 1, 199.99, '', '', 0, NULL),
(9, 9, 9, 5, 19.99, '', '', 0, NULL),
(10, 10, 10, 2, 89.99, '', '', 0, NULL),
(11, 11, 11, 1, 59.99, '', '', 0, NULL),
(12, 12, 12, 1, 129.99, '', '', 0, NULL),
(13, 13, 13, 2, 89.99, '', '', 0, NULL),
(14, 14, 14, 1, 299.99, '', '', 0, NULL),
(15, 15, 15, 1, 109.99, '', '', 0, NULL),
(16, 16, 16, 1, 349.99, '', '', 0, NULL),
(17, 17, 17, 4, 39.99, '', '', 0, NULL),
(18, 18, 18, 2, 29.99, '', '', 0, NULL),
(19, 19, 19, 1, 599.99, '', '', 0, NULL),
(20, 20, 20, 2, 129.99, '', '', 0, NULL),
(21, 21, 1, 2, 99.99, '', '', 0, NULL),
(22, 22, 1, 2, 99.99, '', '', 0, NULL),
(23, 23, 1, 2, 99.99, '', '', 0, NULL),
(24, 24, 1, 2, 99.99, '', '', 0, NULL),
(25, 25, 1, 2, 99.99, '', '', 0, NULL),
(26, 26, 1, 2, 99.99, '', '', 0, NULL),
(27, 27, 1, 2, 99.99, '', '', 0, NULL),
(28, 28, 1, 2, 99.99, '', '', 0, NULL),
(29, 29, 1, 2, 99.99, '', '', 0, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `orders`
--

CREATE TABLE `orders` (
  `order_id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `order_date` timestamp NOT NULL DEFAULT current_timestamp(),
  `status` varchar(20) NOT NULL CHECK (`status` in ('NEW','PAID','SHIPPED','CANCELLED')),
  `file_name` varchar(255) DEFAULT '',
  `content_type` varchar(100) DEFAULT '',
  `file_size` int(11) DEFAULT 0,
  `file_data` mediumblob DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `orders`
--

INSERT INTO `orders` (`order_id`, `customer_id`, `order_date`, `status`, `file_name`, `content_type`, `file_size`, `file_data`) VALUES
(1, 1, '2026-03-02 21:00:19', 'NEW', '', '', 0, NULL),
(2, 2, '2026-03-02 21:00:19', 'PAID', '', '', 0, NULL),
(3, 3, '2026-03-02 21:00:19', 'SHIPPED', '', '', 0, NULL),
(4, 4, '2026-03-02 21:00:19', 'CANCELLED', '', '', 0, NULL),
(5, 5, '2026-03-02 21:00:19', 'NEW', '', '', 0, NULL),
(6, 6, '2026-03-02 21:00:19', 'PAID', '', '', 0, NULL),
(7, 7, '2026-03-02 21:00:19', 'SHIPPED', '', '', 0, NULL),
(8, 8, '2026-03-02 21:00:19', 'NEW', '', '', 0, NULL),
(9, 9, '2026-03-02 21:00:19', 'PAID', '', '', 0, NULL),
(10, 10, '2026-03-02 21:00:19', 'SHIPPED', '', '', 0, NULL),
(11, 11, '2026-03-02 21:00:19', 'NEW', '', '', 0, NULL),
(12, 12, '2026-03-02 21:00:19', 'PAID', '', '', 0, NULL),
(13, 13, '2026-03-02 21:00:19', 'SHIPPED', '', '', 0, NULL),
(14, 14, '2026-03-02 21:00:19', 'NEW', '', '', 0, NULL),
(15, 15, '2026-03-02 21:00:19', 'PAID', '', '', 0, NULL),
(16, 16, '2026-03-02 21:00:19', 'SHIPPED', '', '', 0, NULL),
(17, 17, '2026-03-02 21:00:19', 'NEW', '', '', 0, NULL),
(18, 18, '2026-03-02 21:00:19', 'PAID', '', '', 0, NULL),
(19, 19, '2026-03-02 21:00:19', 'SHIPPED', '', '', 0, NULL),
(20, 20, '2026-03-02 21:00:19', 'NEW', '', '', 0, NULL),
(21, 1, '2026-03-08 02:44:33', 'NEW', '', '', 0, NULL),
(22, 1, '2026-03-08 03:16:50', 'NEW', '', '', 0, NULL),
(23, 1, '2026-03-08 03:17:19', 'NEW', '', '', 0, NULL),
(24, 1, '2026-03-08 03:18:49', 'NEW', '', '', 0, NULL),
(25, 1, '2026-03-08 03:31:05', 'NEW', '', '', 0, NULL),
(26, 1, '2026-03-08 03:33:12', 'NEW', '', '', 0, NULL),
(27, 1, '2026-03-08 03:34:03', 'NEW', '', '', 0, NULL),
(28, 1, '2026-03-08 03:34:24', 'NEW', '', '', 0, NULL),
(29, 1, '2026-03-08 12:49:11', 'NEW', '', '', 0, NULL);

-- --------------------------------------------------------

--
-- Table structure for table `product`
--

CREATE TABLE `product` (
  `product_id` int(11) NOT NULL,
  `product_name` varchar(150) NOT NULL,
  `unit_price` decimal(10,2) NOT NULL CHECK (`unit_price` >= 0),
  `stock_qty` int(11) NOT NULL CHECK (`stock_qty` >= 0),
  `is_active` tinyint(1) NOT NULL DEFAULT 1
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `product`
--

INSERT INTO `product` (`product_id`, `product_name`, `unit_price`, `stock_qty`, `is_active`) VALUES
(1, 'Laptop', 999.99, 10, 1),
(2, 'Smartphone', 699.99, 20, 1),
(3, 'Headphones', 149.99, 30, 1),
(4, 'Keyboard', 79.99, 25, 1),
(5, 'Mouse', 49.99, 40, 1),
(6, 'Monitor', 249.99, 15, 1),
(7, 'Tablet', 399.99, 12, 1),
(8, 'Printer', 199.99, 8, 1),
(9, 'USB Drive', 19.99, 100, 1),
(10, 'earpods', 120.99, 22, 1),
(11, 'Webcam', 59.99, 18, 1),
(12, 'Speaker', 129.99, 14, 1),
(13, 'Router', 89.99, 16, 1),
(14, 'Smartwatch', 299.99, 11, 1),
(15, 'Microphone', 109.99, 13, 1),
(16, 'Gaming Chair', 349.99, 7, 1),
(17, 'Desk Lamp', 39.99, 50, 1),
(18, 'Power Bank', 29.99, 60, 1),
(19, 'Graphics Card', 599.99, 5, 1),
(20, 'SSD 1TB', 129.99, 21, 1);

-- --------------------------------------------------------

--
-- Table structure for table `review`
--

CREATE TABLE `review` (
  `review_id` int(11) NOT NULL,
  `customer_id` int(11) NOT NULL,
  `product_id` int(11) NOT NULL,
  `rating` int(11) NOT NULL CHECK (`rating` between 1 and 5),
  `comment` text DEFAULT NULL,
  `review_date` timestamp NOT NULL DEFAULT current_timestamp()
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `review`
--

INSERT INTO `review` (`review_id`, `customer_id`, `product_id`, `rating`, `comment`, `review_date`) VALUES
(1, 1, 1, 5, 'Excellent product, very happy with the purchase.', '2026-03-14 19:30:55'),
(2, 2, 2, 4, 'Good quality but delivery was a bit slow.', '2026-03-14 19:30:55'),
(3, 3, 3, 5, 'Amazing sound quality, highly recommend.', '2026-03-14 19:30:55'),
(4, 4, 4, 3, 'Keyboard works fine but feels a bit cheap.', '2026-03-14 19:30:55'),
(5, 5, 5, 4, 'Mouse is smooth and responsive.', '2026-03-14 19:30:55'),
(6, 6, 6, 5, 'Monitor display is very sharp and bright.', '2026-03-14 19:30:55'),
(7, 7, 7, 4, 'Tablet works well for daily use.', '2026-03-14 19:30:55'),
(8, 8, 8, 3, 'Printer is okay but setup was complicated.', '2026-03-14 19:30:55'),
(9, 9, 9, 5, 'USB drive is fast and reliable.', '2026-03-14 19:30:55'),
(10, 10, 10, 4, 'External HDD has good storage capacity.', '2026-03-14 19:30:55'),
(11, 11, 11, 5, 'Webcam quality is perfect for meetings.', '2026-03-14 19:30:55'),
(12, 12, 12, 4, 'Speaker sound is clear and loud.', '2026-03-14 19:30:55'),
(13, 13, 13, 3, 'Router works fine but signal range could be better.', '2026-03-14 19:30:55'),
(14, 14, 14, 5, 'Smartwatch has great features and battery life.', '2026-03-14 19:30:55'),
(15, 15, 15, 4, 'Microphone records very clear audio.', '2026-03-14 19:30:55'),
(16, 16, 16, 5, 'Gaming chair is extremely comfortable.', '2026-03-14 19:30:55'),
(17, 17, 17, 4, 'Desk lamp is bright and adjustable.', '2026-03-14 19:30:55'),
(18, 18, 18, 3, 'Power bank charges devices quickly but heats slightly.', '2026-03-14 19:30:55'),
(19, 19, 19, 5, 'Graphics card performance is excellent.', '2026-03-14 19:30:55'),
(20, 20, 20, 4, 'SSD is very fast and improved system performance.', '2026-03-14 19:30:55');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `customer`
--
ALTER TABLE `customer`
  ADD PRIMARY KEY (`customer_id`),
  ADD UNIQUE KEY `email` (`email`),
  ADD KEY `idx_customer_email` (`email`);

--
-- Indexes for table `orderitem`
--
ALTER TABLE `orderitem`
  ADD PRIMARY KEY (`order_item_id`),
  ADD KEY `idx_orderitem_order` (`order_id`),
  ADD KEY `idx_orderitem_product` (`product_id`);

--
-- Indexes for table `orders`
--
ALTER TABLE `orders`
  ADD PRIMARY KEY (`order_id`),
  ADD KEY `idx_orders_customer` (`customer_id`),
  ADD KEY `idx_orders_status` (`status`),
  ADD KEY `idx_orders_date` (`order_date`);

--
-- Indexes for table `product`
--
ALTER TABLE `product`
  ADD PRIMARY KEY (`product_id`),
  ADD KEY `idx_product_name` (`product_name`),
  ADD KEY `idx_product_active` (`is_active`);

--
-- Indexes for table `review`
--
ALTER TABLE `review`
  ADD PRIMARY KEY (`review_id`),
  ADD KEY `idx_review_product` (`product_id`),
  ADD KEY `idx_review_customer` (`customer_id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `customer`
--
ALTER TABLE `customer`
  MODIFY `customer_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=36;

--
-- AUTO_INCREMENT for table `orderitem`
--
ALTER TABLE `orderitem`
  MODIFY `order_item_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT for table `orders`
--
ALTER TABLE `orders`
  MODIFY `order_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT for table `product`
--
ALTER TABLE `product`
  MODIFY `product_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=31;

--
-- AUTO_INCREMENT for table `review`
--
ALTER TABLE `review`
  MODIFY `review_id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=21;

--
-- Constraints for dumped tables
--

--
-- Constraints for table `orderitem`
--
ALTER TABLE `orderitem`
  ADD CONSTRAINT `orderitem_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`),
  ADD CONSTRAINT `orderitem_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`);

--
-- Constraints for table `orders`
--
ALTER TABLE `orders`
  ADD CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`);

--
-- Constraints for table `review`
--
ALTER TABLE `review`
  ADD CONSTRAINT `review_ibfk_1` FOREIGN KEY (`customer_id`) REFERENCES `customer` (`customer_id`),
  ADD CONSTRAINT `review_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `product` (`product_id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
