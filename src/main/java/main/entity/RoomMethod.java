package main.entity;

import javax.persistence.*;

@Entity
public class RoomMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Room room;

    @ManyToOne
    @JoinColumn(nullable = false)
    private Method Method;

    public RoomMethod() {
    }

    public RoomMethod(Room room, Method Method) {
        this.room = room;
        this.Method = Method;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Method getMethod() {
        return Method;
    }

    public void setMethod(Method Method) {
        this.Method = Method;
    }
}