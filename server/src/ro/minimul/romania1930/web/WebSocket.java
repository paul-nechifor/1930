package ro.minimul.romania1930.web;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.Scanner;
import ro.minimul.romania1930.util.Sha;

/**
 * Manages a WebSocket connection.
 * @author Paul Nechifor
 */
public class WebSocket {
    private static final String MAGIC_STRING =
            "258EAFA5-E914-47DA-95CA-C5AB0DC85B11";
    
    private final Socket socket;
    private boolean open;
    private BufferedOutputStream bos;
    private BufferedInputStream bis;
    
    public WebSocket(Socket socket) throws SocketException {
        this.socket = socket;
    }
    
    public void connect() throws IOException {
        if (open) {
            throw new RuntimeException("Already open.");
        }
        
        HashMap<String, String> headers = new HashMap<String, String>();
        
        Scanner s = new Scanner(socket.getInputStream());
        
        // Skip GET.
        s.nextLine();
        
        while (s.hasNextLine()) {
            String line = s.nextLine().trim();
            
            if (line.isEmpty()) {
                break;
            }
            
            String[] parts = line.split(":");
            String name = parts[0].trim().toLowerCase();
            String value = parts[1].trim();
            
            headers.put(name, value);
        }
        
        String key = headers.get("sec-websocket-key");
        
        if (key == null) {
            throw new IOException("No Sec-WebSocket-Key was read.");
        }
        
        String concat = key + MAGIC_STRING;
        String accept = Sha.get1Base64(concat.getBytes());

        String newLine = "\r\n"; // Oppan Windows Style.
        StringBuilder b = new StringBuilder();
        b.append("HTTP/1.1 101 Switching Protocols").append(newLine)
                .append("Upgrade: websocket").append(newLine)
                .append("Connection: Upgrade").append(newLine)
                .append("Sec-WebSocket-Accept: ").append(accept)
                .append(newLine).append(newLine);
        
        OutputStream out = socket.getOutputStream();
        out.write(b.toString().getBytes());
        out.flush();
        
        bos = new BufferedOutputStream(out);
        bis = new BufferedInputStream(socket.getInputStream());
        
        open = true;
    }
    
    public void clientConnect() throws IOException {
        if (open) {
            throw new RuntimeException("Already open.");
        }
        
        bos = new BufferedOutputStream(socket.getOutputStream());
        bis = new BufferedInputStream(socket.getInputStream());
        
        open = true;
    }
    
    public boolean isOpen() {
        return open;
    }
    
    /**
     * Sends a non-masked single frame text message to this web socket.
     * @param message
     * @author Paul Nechifor
     * @throws IOException 
     */
    public void write(byte[] message) throws IOException {
        byte[] head;
        int length = message.length;
        
        if (length < 125) {
            head = new byte[2];
            head[1] = (byte) length; // The mask bit is already off.
        } else if (length <= 0xFFFF) {
            head = new byte[4];
            head[1] = 126;
            head[2] = (byte) (length >> 8);
            head[3] = (byte) (length & 0xFF);
        } else {
            head = new byte[10];
            head[1] = 127;

            // I'm writing an int to a long, so I ignore the first 4 bytes.
            head[9] = (byte)(length & 0xFF);
            length >>= 8;
            head[8] = (byte)(length & 0xFF);
            length >>= 8;
            head[7] = (byte)(length & 0xFF);
            length >>= 8;
            head[6] = (byte)(length & 0xFF);
        }
        
        head[0] = (byte) 0x81; // Text frame.
        
        bos.write(head);
        bos.write(message);
        bos.flush();
    }
    
    public void close() throws IOException {
        // Closing the socket without sending a closing frame.
        bos.close();
        bis.close();
        socket.close();
        
        open = false;
    }
    
    /**
     * Reads a single frame message as a text message.
     * @return The message, or null if the socket was closed.
     * @throws IOException
     */
    public byte[] read() throws IOException {
        // Reading the first two bytes.
        byte[] twoBytes = new byte[2];
        bis.read(twoBytes);
        
        // Checking if it is a closing frame.
        if ((twoBytes[0] & 0x08) != 0) {
            close();
            return null;
        }
        
        // twoBytes[0] should be 0x81, but whatever.
        
        boolean masked = (twoBytes[1] & 0x80) > 0;
        
        twoBytes[1] &= 0x7F; // Removing the mask bit.
        
        long length = 0;
        if (twoBytes[1] <= 125) {
            length = twoBytes[1];
        } else if (twoBytes[1] == 126) {
            // Reusing the same byte array.
            bis.read(twoBytes);
            length = ((twoBytes[0]&0xff) << 8) | (twoBytes[1]&0xff);
        } else {
            byte[] eight = new byte[8];
            bis.read(eight);
            
            for (int i = 0; i < 8; i++) {
                length = (length << 8) | eight[i];
            }
        }
        
        // I should probably check if length <= Integer.MAX_VALUE.
        
        byte[] mask = null;
        if (masked) {
            mask = new byte[4];
            bis.read(mask);
        }
        
        byte[] message = new byte[(int) length];
        bis.read(message);
        
        if (masked) {
            // Unmasking the message.
            for (int i = 0; i < message.length; i++) {
                message[i] ^= mask[i % 4];
            }
        }
        
        return message;
    }
}