USE [master]
GO
/****** Object:  Database [Aquadis]    Script Date: 2/24/2025 1:15:06 PM ******/
CREATE DATABASE [Aquadis]
 CONTAINMENT = NONE
 ON  PRIMARY 
( NAME = N'Aquadis', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL16.JOSEPHMOUHAYAR\MSSQL\DATA\Aquadis.mdf' , SIZE = 8192KB , MAXSIZE = UNLIMITED, FILEGROWTH = 65536KB )
 LOG ON 
( NAME = N'Aquadis_log', FILENAME = N'C:\Program Files\Microsoft SQL Server\MSSQL16.JOSEPHMOUHAYAR\MSSQL\DATA\Aquadis_log.ldf' , SIZE = 8192KB , MAXSIZE = 2048GB , FILEGROWTH = 65536KB )
 WITH CATALOG_COLLATION = DATABASE_DEFAULT, LEDGER = OFF
GO
ALTER DATABASE [Aquadis] SET COMPATIBILITY_LEVEL = 160
GO
IF (1 = FULLTEXTSERVICEPROPERTY('IsFullTextInstalled'))
begin
EXEC [Aquadis].[dbo].[sp_fulltext_database] @action = 'enable'
end
GO
ALTER DATABASE [Aquadis] SET ANSI_NULL_DEFAULT OFF 
GO
ALTER DATABASE [Aquadis] SET ANSI_NULLS OFF 
GO
ALTER DATABASE [Aquadis] SET ANSI_PADDING OFF 
GO
ALTER DATABASE [Aquadis] SET ANSI_WARNINGS OFF 
GO
ALTER DATABASE [Aquadis] SET ARITHABORT OFF 
GO
ALTER DATABASE [Aquadis] SET AUTO_CLOSE OFF 
GO
ALTER DATABASE [Aquadis] SET AUTO_SHRINK OFF 
GO
ALTER DATABASE [Aquadis] SET AUTO_UPDATE_STATISTICS ON 
GO
ALTER DATABASE [Aquadis] SET CURSOR_CLOSE_ON_COMMIT OFF 
GO
ALTER DATABASE [Aquadis] SET CURSOR_DEFAULT  GLOBAL 
GO
ALTER DATABASE [Aquadis] SET CONCAT_NULL_YIELDS_NULL OFF 
GO
ALTER DATABASE [Aquadis] SET NUMERIC_ROUNDABORT OFF 
GO
ALTER DATABASE [Aquadis] SET QUOTED_IDENTIFIER OFF 
GO
ALTER DATABASE [Aquadis] SET RECURSIVE_TRIGGERS OFF 
GO
ALTER DATABASE [Aquadis] SET  DISABLE_BROKER 
GO
ALTER DATABASE [Aquadis] SET AUTO_UPDATE_STATISTICS_ASYNC OFF 
GO
ALTER DATABASE [Aquadis] SET DATE_CORRELATION_OPTIMIZATION OFF 
GO
ALTER DATABASE [Aquadis] SET TRUSTWORTHY OFF 
GO
ALTER DATABASE [Aquadis] SET ALLOW_SNAPSHOT_ISOLATION OFF 
GO
ALTER DATABASE [Aquadis] SET PARAMETERIZATION SIMPLE 
GO
ALTER DATABASE [Aquadis] SET READ_COMMITTED_SNAPSHOT OFF 
GO
ALTER DATABASE [Aquadis] SET HONOR_BROKER_PRIORITY OFF 
GO
ALTER DATABASE [Aquadis] SET RECOVERY FULL 
GO
ALTER DATABASE [Aquadis] SET  MULTI_USER 
GO
ALTER DATABASE [Aquadis] SET PAGE_VERIFY CHECKSUM  
GO
ALTER DATABASE [Aquadis] SET DB_CHAINING OFF 
GO
ALTER DATABASE [Aquadis] SET FILESTREAM( NON_TRANSACTED_ACCESS = OFF ) 
GO
ALTER DATABASE [Aquadis] SET TARGET_RECOVERY_TIME = 60 SECONDS 
GO
ALTER DATABASE [Aquadis] SET DELAYED_DURABILITY = DISABLED 
GO
ALTER DATABASE [Aquadis] SET ACCELERATED_DATABASE_RECOVERY = OFF  
GO
EXEC sys.sp_db_vardecimal_storage_format N'Aquadis', N'ON'
GO
ALTER DATABASE [Aquadis] SET QUERY_STORE = ON
GO
ALTER DATABASE [Aquadis] SET QUERY_STORE (OPERATION_MODE = READ_WRITE, CLEANUP_POLICY = (STALE_QUERY_THRESHOLD_DAYS = 30), DATA_FLUSH_INTERVAL_SECONDS = 900, INTERVAL_LENGTH_MINUTES = 60, MAX_STORAGE_SIZE_MB = 1000, QUERY_CAPTURE_MODE = AUTO, SIZE_BASED_CLEANUP_MODE = AUTO, MAX_PLANS_PER_QUERY = 200, WAIT_STATS_CAPTURE_MODE = ON)
GO
USE [Aquadis]
GO
/****** Object:  Table [dbo].[BankAccounts]    Script Date: 2/24/2025 1:15:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[BankAccounts](
	[BankAccountID] [bigint] IDENTITY(1,1) NOT NULL,
	[Amount] [real] NOT NULL,
	[CustomerID] [bigint] NULL,
PRIMARY KEY CLUSTERED 
(
	[BankAccountID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Categories]    Script Date: 2/24/2025 1:15:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Categories](
	[CategoryID] [bigint] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[CategoryID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Customers]    Script Date: 2/24/2025 1:15:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Customers](
	[CustomerID] [bigint] IDENTITY(1,1) NOT NULL,
	[Name] [varchar](255) NULL,
	[Password] [varchar](255) NULL,
PRIMARY KEY CLUSTERED 
(
	[CustomerID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
/****** Object:  Table [dbo].[Transactions]    Script Date: 2/24/2025 1:15:06 PM ******/
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
CREATE TABLE [dbo].[Transactions](
	[TransactionID] [bigint] IDENTITY(1,1) NOT NULL,
	[Amount] [real] NOT NULL,
	[Type] [varchar](255) NULL,
	[createdAt] [datetime2](6) NULL,
	[BankAccountID] [bigint] NOT NULL,
	[CategoryID] [bigint] NOT NULL,
PRIMARY KEY CLUSTERED 
(
	[TransactionID] ASC
)WITH (PAD_INDEX = OFF, STATISTICS_NORECOMPUTE = OFF, IGNORE_DUP_KEY = OFF, ALLOW_ROW_LOCKS = ON, ALLOW_PAGE_LOCKS = ON, OPTIMIZE_FOR_SEQUENTIAL_KEY = OFF) ON [PRIMARY]
) ON [PRIMARY]
GO
SET IDENTITY_INSERT [dbo].[BankAccounts] ON 

INSERT [dbo].[BankAccounts] ([BankAccountID], [Amount], [CustomerID]) VALUES (11, 600, 4)
SET IDENTITY_INSERT [dbo].[BankAccounts] OFF
GO
SET IDENTITY_INSERT [dbo].[Categories] ON 

INSERT [dbo].[Categories] ([CategoryID], [Name]) VALUES (1, N'Car Maintenance')
INSERT [dbo].[Categories] ([CategoryID], [Name]) VALUES (2, N'Wage')
INSERT [dbo].[Categories] ([CategoryID], [Name]) VALUES (3, N'Food & Dining')
INSERT [dbo].[Categories] ([CategoryID], [Name]) VALUES (4, N'Transportation')
INSERT [dbo].[Categories] ([CategoryID], [Name]) VALUES (5, N'Entertainment')
INSERT [dbo].[Categories] ([CategoryID], [Name]) VALUES (6, N'Utilities')
INSERT [dbo].[Categories] ([CategoryID], [Name]) VALUES (7, N'Health & Fitness')
SET IDENTITY_INSERT [dbo].[Categories] OFF
GO
SET IDENTITY_INSERT [dbo].[Customers] ON 

INSERT [dbo].[Customers] ([CustomerID], [Name], [Password]) VALUES (4, N'Joseph', N'1c1bb7108976e786896ca9e9435a5f24d11d066d8346c0fa51631085b88e6c5b')
INSERT [dbo].[Customers] ([CustomerID], [Name], [Password]) VALUES (5, N'Joe', N'1c1bb7108976e786896ca9e9435a5f24d11d066d8346c0fa51631085b88e6c5b')
SET IDENTITY_INSERT [dbo].[Customers] OFF
GO
SET IDENTITY_INSERT [dbo].[Transactions] ON 

INSERT [dbo].[Transactions] ([TransactionID], [Amount], [Type], [createdAt], [BankAccountID], [CategoryID]) VALUES (19, 500, N'income', CAST(N'2025-01-31T08:25:00.4813910' AS DateTime2), 11, 3)
INSERT [dbo].[Transactions] ([TransactionID], [Amount], [Type], [createdAt], [BankAccountID], [CategoryID]) VALUES (21, 700, N'expense', CAST(N'2025-01-31T08:25:23.8400150' AS DateTime2), 11, 5)
INSERT [dbo].[Transactions] ([TransactionID], [Amount], [Type], [createdAt], [BankAccountID], [CategoryID]) VALUES (22, 300, N'expense', CAST(N'2025-01-31T08:25:45.7703690' AS DateTime2), 11, 5)
INSERT [dbo].[Transactions] ([TransactionID], [Amount], [Type], [createdAt], [BankAccountID], [CategoryID]) VALUES (23, 800, N'income', CAST(N'2025-01-31T08:25:54.1750160' AS DateTime2), 11, 2)
INSERT [dbo].[Transactions] ([TransactionID], [Amount], [Type], [createdAt], [BankAccountID], [CategoryID]) VALUES (24, 200, N'expense', CAST(N'2025-01-31T08:26:05.8625310' AS DateTime2), 11, 6)
INSERT [dbo].[Transactions] ([TransactionID], [Amount], [Type], [createdAt], [BankAccountID], [CategoryID]) VALUES (25, 500, N'income', CAST(N'2025-01-31T14:39:50.0207490' AS DateTime2), 11, 1)
SET IDENTITY_INSERT [dbo].[Transactions] OFF
GO
ALTER TABLE [dbo].[BankAccounts]  WITH CHECK ADD  CONSTRAINT [FKbpppqt6wex23i8694uhh3ket6] FOREIGN KEY([CustomerID])
REFERENCES [dbo].[Customers] ([CustomerID])
GO
ALTER TABLE [dbo].[BankAccounts] CHECK CONSTRAINT [FKbpppqt6wex23i8694uhh3ket6]
GO
ALTER TABLE [dbo].[Transactions]  WITH CHECK ADD  CONSTRAINT [FK6ow57562mjs81xgr1169p1mc6] FOREIGN KEY([BankAccountID])
REFERENCES [dbo].[BankAccounts] ([BankAccountID])
GO
ALTER TABLE [dbo].[Transactions] CHECK CONSTRAINT [FK6ow57562mjs81xgr1169p1mc6]
GO
ALTER TABLE [dbo].[Transactions]  WITH CHECK ADD  CONSTRAINT [FKtfyt8dbhja66hteo1j0t41r0o] FOREIGN KEY([CategoryID])
REFERENCES [dbo].[Categories] ([CategoryID])
GO
ALTER TABLE [dbo].[Transactions] CHECK CONSTRAINT [FKtfyt8dbhja66hteo1j0t41r0o]
GO
USE [master]
GO
ALTER DATABASE [Aquadis] SET  READ_WRITE 
GO
