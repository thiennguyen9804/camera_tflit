precision mediump float;  // xác định độ chính xác trung bình cho biến float

varying mediump vec2 vtexcoord; // vtexcoord có kiểu vec2 độ chính xác trung bình và được đưa ra ngoài
attribute mediump vec4 position; // biến position chứa tọa độ từng đỉnh có kiểu vec4 và là biến đầu vào
attribute mediump vec2 texcoord; // biến texcoord chứa tọa độ từng texture và là biến đầu vào

void main() {
    gl_Position = position;
    vtexcoord = texcoord;
}
