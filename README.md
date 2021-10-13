# Step-by-step decoding algorithm

This program implements three encoding//decoding scenarios to showcase the Step-by-step decoding alogrithm.
In all scenarios the message vectors are encoded using linear \[N, K] code, with a mistake probability P<sub>e</sub> that i-th bit position of vector will be changed when the vector is sent through the channel.  

The program was written with Kotlin. For the GUI development TornadoFX library was used. It was developed during Coding theory course at Vilnius University.

##### Scenarios:
- First - K bit message vector is encoded and decoded.
- Second - a string text is converted to K bit message vectors which are encoded and decoded. In the end message vectors are combined and converted back to string text.
- Third - a bmp format image is converted to K bit message vectors which are encoded and decoded. In the end message vectors are combined and converted back to bmp image.
