# Step-by-step decoding algorithm

This program implements three encoding//decoding scenarios to showcase the Step-by-step decoding alogrithm.
In all scenarios the message vectors are encoded using linear \[N, K] code, with a mistake probability P<sub>e</sub> that i-th bit position of vector will be changed when the vector is sent through the channel.  

The program was written with Kotlin. For the GUI development TornadoFX library was used. It was developed during Coding theory course at Vilnius University.

##### Scenarios:
- First - K bit message vector is encoded and decoded.
- Second - a string text is converted to K bit message vectors which are encoded and decoded. In the end message vectors are combined and converted back to string text.
- Third - a bmp format image is converted to K bit message vectors which are encoded and decoded. In the end message vectors are combined and converted back to bmp image.
  
##### Experiments:
After the development of the program, two experiments were carried out to analyze the code's effectiveness.
- First experiment - during this experiment different N and K parameters were used to check the effectiveness of coded messages against uncoded messages, when sent through the cannel. That means each time the message was received from channel it was compared with original message by calculating how many bits they are different. The N parameter was chosen from interval \[3;15], while K parameter was chosen from \[2;N-1]. The mistake probability P<sub>e</sub> through each iteration was constant, it's value was 0.01. The generator matrix was filled randomly each iteration.
- Second experiment - during this experiment different P<sub>e</sub> parameter was used to check how mistake probability affects code's effectiveness. P<sub>e</sub> was chosen from interval \[0.0001;0.5] with incremental step of 0.005. N and K parameters were constant values, 5 and 3 respectively. Generator matrix was filled randomly each iteration
