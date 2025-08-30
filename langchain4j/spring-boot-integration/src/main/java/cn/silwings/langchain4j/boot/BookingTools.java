package cn.silwings.langchain4j.boot;

import dev.langchain4j.agent.tool.Tool;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class BookingTools {

    private final List<Booking> bookingList = new ArrayList<>();

    @Tool
    public Booking getBookingDetails(final String bookingNumber, final String customerName, final String customerSurname) {
        // 查找匹配的预订
        Optional<Booking> matchingBooking = this.bookingList.stream()
                .filter(booking -> booking.getBookingNumber().equals(bookingNumber) &&
                                   booking.getCustomerName().equalsIgnoreCase(customerName) &&
                                   booking.getCustomerSurname().equalsIgnoreCase(customerSurname))
                .findFirst();

        return matchingBooking.orElse(null);
    }

    @Tool
    public List<Booking> getBookings() {
        return this.bookingList.stream().filter(e -> e.getStatus().equals("CONFIRMED")).toList();
    }

    @Tool
    public void cancelBooking(String bookingNumber, String customerName, String customerSurname) {
        // 查找并取消匹配的预订
        this.bookingList.stream()
                .filter(booking -> booking.getBookingNumber().equals(bookingNumber) &&
                                   booking.getCustomerName().equalsIgnoreCase(customerName) &&
                                   booking.getCustomerSurname().equalsIgnoreCase(customerSurname))
                .findFirst()
                .ifPresent(booking -> booking.setStatus("CANCELLED"));
    }

    @Getter
    @Setter
    public static class Booking {
        private String bookingNumber;
        private String customerName;
        private String customerSurname;
        private LocalDate bookingDate;
        private LocalDate checkInDate;
        private LocalDate checkOutDate;
        private String status; // 例如: "CONFIRMED", "CANCELLED", "PENDING"

        // 构造函数
        public Booking(String bookingNumber, String customerName, String customerSurname,
                       LocalDate bookingDate, LocalDate checkInDate, LocalDate checkOutDate) {
            this.bookingNumber = bookingNumber;
            this.customerName = customerName;
            this.customerSurname = customerSurname;
            this.bookingDate = bookingDate;
            this.checkInDate = checkInDate;
            this.checkOutDate = checkOutDate;
            this.status = "CONFIRMED"; // 默认状态为已确认
        }
    }

    @PostConstruct
    public void init() {
        // 初始化三个预订实例并添加到集合中
        LocalDate today = LocalDate.now();

        bookingList.add(new Booking(
                "BK001",
                "John",
                "Doe",
                today.minusDays(7),
                today.plusDays(3),
                today.plusDays(10)
        ));

        bookingList.add(new Booking(
                "BK002",
                "Jane",
                "Smith",
                today.minusDays(14),
                today.plusDays(15),
                today.plusDays(20)
        ));

        bookingList.add(new Booking(
                "BK003",
                "Michael",
                "Johnson",
                today.minusDays(5),
                today.plusDays(2),
                today.plusDays(5)
        ));
    }
}
