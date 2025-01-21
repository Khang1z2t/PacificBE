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
    @Action NVARCHAR(10),  -- Tham số chỉ định hành động (Add, Update, Delete)
    @TourID INT = NULL,    -- Tham số ID của tour (chỉ cần khi cập nhật hoặc xóa)
    @TourName NVARCHAR(255) = NULL,
    @Destination NVARCHAR(255) = NULL,
    @StartDate DATE = NULL,
    @EndDate DATE = NULL,
    @Price DECIMAL(18, 2) = NULL,
    @Capacity INT = NULL,
    @Description NVARCHAR(MAX) = NULL,
    @ImageURL NVARCHAR(255) = NULL,
    @StatusID INT = NULL
AS
BEGIN
    -- Kiểm tra hành động và thực hiện tương ứng
    IF @Action = 'Add'
    BEGIN
        -- Thêm một tour mới 
        INSERT INTO Tours (TourName, Destination, StartDate, EndDate, Price, Capacity, Description, ImageURL, StatusID)
        VALUES (@TourName, @Destination, @StartDate, @EndDate, @Price, @Capacity, @Description, @ImageURL, @StatusID);
        SELECT 'Tour đã được thêm thành công!' AS Message;
    END

    ELSE IF @Action = 'Update'
    BEGIN
        -- Cập nhật thông tin của một tour
        IF @TourID IS NULL
        BEGIN
            SELECT 'TourID là bắt buộc khi cập nhật.' AS ErrorMessage;
            RETURN;
        END
        UPDATE Tours
        SET 
            TourName = ISNULL(@TourName, TourName),
            Destination = ISNULL(@Destination, Destination),
            StartDate = ISNULL(@StartDate, StartDate),
            EndDate = ISNULL(@EndDate, EndDate),
            Price = ISNULL(@Price, Price),
            Capacity = ISNULL(@Capacity, Capacity),
            Description = ISNULL(@Description, Description),
            ImageURL = ISNULL(@ImageURL, ImageURL),
            StatusID = ISNULL(@StatusID, StatusID)
        WHERE TourID = @TourID;
        SELECT 'Tour đã được cập nhật thành công!' AS Message;
    END

    ELSE IF @Action = 'Delete'
    BEGIN
        -- Xóa một tour
        IF @TourID IS NULL
        BEGIN
            SELECT 'TourID là bắt buộc khi xóa.' AS ErrorMessage;
            RETURN;
        END
        DELETE FROM Tours WHERE TourID = @TourID;
        SELECT 'Tour đã được xóa thành công!' AS Message;
    END

    ELSE
    BEGIN
        SELECT 'Hành động không hợp lệ. Vui lòng chọn Add, Update hoặc Delete.' AS ErrorMessage;
    END
END;

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
    INSERT INTO Users (FullName, Email, PhoneNumber, Password, RoleID, StatusID)
    VALUES (@FullName, @Email, @PhoneNumber, @Password, @RoleID, @StatusID);

    SELECT 'Người dùng đã được thêm thành công!' AS SuccessMessage;
END;


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
        IF NOT EXISTS (SELECT 1 FROM Users WHERE UserID = @UserID)
        BEGIN
            PRINT 'Error: UserID does not exist.';
            RETURN;
        END

        -- Truy xuất dữ liệu đặt chỗ
		select
			u.UserID
			,u.FullName
			,b.BookingDate
			,b.TotalPrice
			,b.TotalPersons
			,t.TourName
			,b.StatusID
			,st.StatusName
			,b.BookingID
			,r.RegionName
			,tt.TourTypeName
			,b.StatusID
			,st.StatusName
		from Users u
			join Bookings b on u.UserID = b.UserID
			join BookingDetails bd on b.BookingID = bd.BookingID
			join Tours t on t.TourID = b.TourID
			join Statuses st on st.StatusID = b.StatusID
			join Regions r on r.RegionID = t.RegionID
			join TourTypes tt on tt.TourTypeID = t.TourTypeID
		where u.UserID = @UserID
		group by 
			b.BookingID
			,u.UserID
			,u.FullName
			,b.BookingDate
			,b.TotalPrice
			,b.TotalPersons
			,t.TourName
			,b.StatusID
			,st.StatusName
			,r.RegionName
			,tt.TourTypeName
		order by b.BookingID;

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
    JOIN Regions r ON r.RegionID = t.RegionID
    JOIN TourTypes tt ON tt.TourTypeID = t.TourTypeID
    WHERE
        (@TourTypeID IS NULL OR tt.TourTypeID = @TourTypeID)  -- Nếu TourTypeID là NULL, bỏ qua điều kiện này
    AND
        (@RegionID IS NULL OR r.RegionID = @RegionID);        -- Nếu RegionID là NULL, bỏ qua điều kiện này
END;

-- Procedure tìm kiếm lịch trình theo tour
IF OBJECT_ID('dbo.GetScheduleByTourID', 'P') IS NOT NULL
    DROP PROCEDURE dbo.GetScheduleByTourID;
GO

CREATE PROCEDURE GetScheduleByTourID
    @TourID INT  -- Tham số đầu vào là TourID
AS
BEGIN
    SELECT 
        s.DayNumber,
        s.StartTime,
        s.EndTime,
        s.Activity,
        s.Description,
        s.TourID,
        t.TourName
    FROM Tours t
    LEFT JOIN Schedule s ON t.TourID = s.TourID
    WHERE t.TourID = @TourID;  -- Lọc theo TourID được truyền vào
END;

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
        AND (@StartDate IS NULL OR StartDate >= @StartDate)  -- Nếu @StartDate là NULL thì không áp dụng điều kiện này
        AND (@EndDate IS NULL OR EndDate <= @EndDate);  -- Nếu @EndDate là NULL thì không áp dụng điều kiện này
END;
