-- ================================================
-- Template generated from Template Explorer using:
-- Create Procedure (New Menu).SQL
--
-- Use the Specify Values for Template Parameters 
-- command (Ctrl-Shift-M) to fill in the parameter 
-- values below.
--
-- This block of comments will not be included in
-- the definition of the procedure.
-- ================================================
SET ANSI_NULLS ON
GO
SET QUOTED_IDENTIFIER ON
GO
-- =============================================
-- Author:		<Chương>
-- Create date: <Create Date,01/20/2025>
-- Description:	<Description, Các proc trong csdl pacific_tour>
-- =============================================

-- Procedure thêm tour
-- Kiểm tra và xóa Stored Procedure nếu đã tồn tại
IF OBJECT_ID('dbo.ManageTour', 'P') IS NOT NULL
    DROP PROCEDURE dbo.ManageTour;
GO
CREATE PROCEDURE dbo.ManageTour
    @Action NVARCHAR(10), -- Loại thao tác: 'INSERT', 'UPDATE', 'DELETE'
    @TourID INT = NULL, -- ID của tour (NULL khi thêm mới)
    @TourName NVARCHAR(255) = NULL,
    @Destination NVARCHAR(255) = NULL,
    @StartDate DATE = NULL,
    @EndDate DATE = NULL,
    @Price DECIMAL(18, 2) = NULL,
    @Capacity INT = NULL,
    @Description NVARCHAR(MAX) = NULL,
    @ImageURL NVARCHAR(255) = NULL,
    @StatusID INT = NULL,
    @RegionID INT = NULL,
    @TourTypeID INT = NULL
AS
BEGIN
    SET NOCOUNT ON;

    IF @Action = 'INSERT'
    BEGIN
        INSERT INTO Tours (tour_name, Destination, start_date, end_date, Price, Capacity, Description, image_url, status_id, region_id, tour_type_id)
        VALUES (@TourName, @Destination, @StartDate, @EndDate, @Price, @Capacity, @Description, @ImageURL, @StatusID, @RegionID, @TourTypeID);
        
        PRINT 'Tour inserted successfully.';
    END
    ELSE IF @Action = 'UPDATE'
    BEGIN
        IF @TourID IS NULL
        BEGIN
            PRINT 'Error: TourID is required for UPDATE.';
            RETURN;
        END
        
        UPDATE Tours
        SET 
            tour_name = @TourName,
            Destination = @Destination,
            start_date = @StartDate,
            end_date = @EndDate,
            Price = @Price,
            Capacity = @Capacity,
            Description = @Description,
            image_url = @ImageURL,
            status_id = @StatusID,
            region_id = @RegionID,
            tour_type_id = @TourTypeID
        WHERE Tour_ID = @TourID;
        
        PRINT 'Tour updated successfully.';
    END
    ELSE IF @Action = 'DELETE'
    BEGIN
        IF @TourID IS NULL
        BEGIN
            PRINT 'Error: TourID is required for DELETE.';
            RETURN;
        END
        
        DELETE FROM Tours WHERE tour_id = @TourID;
        
        PRINT 'Tour deleted successfully.';
    END
    ELSE
    BEGIN
        PRINT 'Error: Invalid Action. Use INSERT, UPDATE, or DELETE.';
    END
END;
GO

-- Procedure thêm users
-- Kiểm tra và xóa Stored Procedure nếu đã tồn tại
IF OBJECT_ID('dbo.AddUser', 'P') IS NOT NULL
    DROP PROCEDURE dbo.AddUser;
GO
CREATE PROCEDURE dbo.AddUser
    @FullName NVARCHAR(255),
    @Email NVARCHAR(255),
    @PhoneNumber NVARCHAR(20) = NULL,  -- Có thể để trống
    @Password NVARCHAR(255),
    @RoleID TINYINT,
    @StatusID INT
AS
BEGIN
    -- Kiểm tra xem email đã tồn tại chưa
    IF EXISTS (SELECT 1 FROM Users WHERE Email = @Email)
    BEGIN
        SELECT 'Email đã tồn tại trong hệ thống. Vui lòng chọn email khác.' AS ErrorMessage;
        RETURN;
    END

    -- Thêm người dùng mới vào bảng Users
    INSERT INTO Users (full_name, Email, phone_number, Password, role_id, status_id)
    VALUES (@FullName, @Email, @PhoneNumber, @Password, @RoleID, @StatusID);

    SELECT 'Người dùng đã được thêm thành công!' AS SuccessMessage;
END;
GO

-- Procedure tìm kiếm booking của người dùng
IF OBJECT_ID('dbo.GetUserBookings', 'P') IS NOT NULL
    DROP PROCEDURE dbo.GetUserBookings;
GO
CREATE PROCEDURE dbo.GetUserBookings
    @UserID INT
AS
BEGIN
    SET NOCOUNT ON;

    BEGIN TRY
        -- Kiểm tra xem UserID có tồn tại không
        IF NOT EXISTS (SELECT 1 FROM Users WHERE user_id = @UserID)
        BEGIN
            PRINT 'Error: UserID does not exist.';
            RETURN;
        END

        -- Truy xuất dữ liệu đặt chỗ
		select
			u.user_id
			,u.full_name
			,b.booking_date
			,b.total_price
			,b.total_persons
			,t.tour_name
			,b.status_id
			,st.status_name
			,b.booking_id
			,r.region_name
			,tt.tour_type_name
			,b.status_id
			,st.status_name
		from Users u
			join Bookings b on u.user_id = b.user_id
			join booking_details bd on b.booking_id = bd.booking_id
			join Tours t on t.tour_id = b.tour_id
			join Statuses st on st.status_id = b.status_id
			join Regions r on r.region_id = t.region_id
			join tour_types tt on tt.tour_type_id = t.tour_type_id
		where u.user_id = @UserID
		group by 
			b.booking_id
			,u.user_id
			,u.full_name
			,b.booking_date
			,b.total_price
			,b.total_persons
			,t.tour_name
			,b.status_id
			,st.status_name
			,r.region_name
			,tt.tour_type_name
		order by b.booking_id;

        -- Kiểm tra nếu không có kết quả trả về
        IF @@ROWCOUNT = 0
        BEGIN
            PRINT 'No bookings found for the specified UserID.';
        END
    END TRY
    BEGIN CATCH
        -- Xử lý lỗi trong quá trình thực thi
        PRINT 'An error occurred. Please check the input parameters or database state.';
        PRINT ERROR_MESSAGE(); -- In ra thông báo lỗi
    END CATCH
END;
GO

-- Procedure tìm kiếm tour theo vùng miền, trong ngoài nước
IF OBJECT_ID('dbo.SearchToursByTypeAndRegion', 'P') IS NOT NULL
    DROP PROCEDURE dbo.SearchToursByTypeAndRegion;
GO
CREATE PROCEDURE SearchToursByTypeAndRegion
    @TourTypeID INT = NULL,  -- Cho phép giá trị NULL cho TourTypeID
    @RegionID INT = NULL     -- Cho phép giá trị NULL cho RegionID
AS
BEGIN
    SELECT
        t.*,
        r.*,
        tt.*
    FROM Tours t
    JOIN Regions r ON r.region_id = t.region_id
    JOIN tour_types tt ON tt.tour_type_id = t.tour_type_id
    WHERE
        (@TourTypeID IS NULL OR tt.tour_type_id = @TourTypeID)  -- Nếu TourTypeID là NULL, bỏ qua điều kiện này
    AND
        (@RegionID IS NULL OR r.region_id = @RegionID);        -- Nếu RegionID là NULL, bỏ qua điều kiện này
END;
GO

-- Procedure tìm kiếm lịch trình theo tour
IF OBJECT_ID('dbo.GetScheduleByTourID', 'P') IS NOT NULL
    DROP PROCEDURE dbo.GetScheduleByTourID;
GO

CREATE PROCEDURE GetScheduleByTourID
    @TourID INT  -- Tham số đầu vào là TourID
AS
BEGIN
    SELECT 
        s.Day_Number,
        s.Start_Time,
        s.End_Time,
        s.Activity,
        s.Description,
        s.Tour_ID,
        t.tour_name
    FROM Tours t
    LEFT JOIN schedules s ON t.tour_id = s.Tour_ID
    WHERE t.tour_id = @TourID;  -- Lọc theo TourID được truyền vào
END;
GO

-- Procedure tìm kiếm tour theo giá, ngày
IF OBJECT_ID('dbo.GetToursByPriceAndDate', 'P') IS NOT NULL
    DROP PROCEDURE dbo.GetToursByPriceAndDate;
GO
CREATE PROCEDURE GetToursByPriceAndDate
    @MinPrice DECIMAL(18, 2) = NULL,  -- Tham số giá tối thiểu, mặc định là NULL
    @MaxPrice DECIMAL(18, 2) = NULL,  -- Tham số giá tối đa, mặc định là NULL
    @StartDate DATE = NULL,           -- Tham số ngày bắt đầu, mặc định là NULL
    @EndDate DATE = NULL              -- Tham số ngày kết thúc, mặc định là NULL
AS
BEGIN
    SELECT * 
    FROM Tours
    WHERE 
        (@MinPrice IS NULL OR Price >= @MinPrice)  -- Nếu @MinPrice là NULL thì không áp dụng điều kiện này
        AND (@MaxPrice IS NULL OR Price <= @MaxPrice)  -- Nếu @MaxPrice là NULL thì không áp dụng điều kiện này
        AND (@StartDate IS NULL OR start_date >= @StartDate)  -- Nếu @StartDate là NULL thì không áp dụng điều kiện này
        AND (@EndDate IS NULL OR end_date <= @EndDate);  -- Nếu @EndDate là NULL thì không áp dụng điều kiện này
END;
GO