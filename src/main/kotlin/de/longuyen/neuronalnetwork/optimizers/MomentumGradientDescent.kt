package de.longuyen.neuronalnetwork.optimizers

import org.nd4j.linalg.api.ndarray.INDArray
import org.nd4j.linalg.factory.Nd4j
import java.io.Serializable

class MomentumGradientDescent(private val learningRate: Double, private val momentum: Double = 0.9) : Optimizer(), Serializable {
    companion object {
        private const val serialVersionUID: Long = 1
    }
    private var initialized = false
    private val velocity = mutableMapOf<String, INDArray>()

    override fun optimize(weights: MutableMap<String, INDArray>, gradients: MutableMap<String, INDArray>, layers: Int) {
        if(!initialized){
            for(i in 2 until layers){
                velocity["vW$i"] = Nd4j.zeros(*weights["W$i"]!!.shape())
                velocity["vb$i"] = Nd4j.zeros(*weights["b$i"]!!.shape())
            }
            initialized = true
        }
        for(i in 2 until layers){
            velocity["vW$i"] = velocity["vW$i"]!!.mul(momentum)
            velocity["vW$i"] = gradients["dW$i"]!!.mul(learningRate)
            weights["W$i"] = weights["W$i"]!!.sub(velocity["vW$i"])

            velocity["vb$i"] = velocity["vb$i"]!!.mul(momentum)
            velocity["vb$i"] = gradients["db$i"]!!.mul(learningRate)
            weights["b$i"] = weights["b$i"]!!.sub(velocity["vb$i"])
        }
    }

}