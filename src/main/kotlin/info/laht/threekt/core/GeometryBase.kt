package info.laht.threekt.core

import info.laht.threekt.math.*

abstract class GeometryBase<E>: EventDispatcher() {

    var name = ""
    val uuid = generateUUID()

    var boundingBox: Box3? = null
        protected set
    var boundingSphere: Sphere? = null
        protected set

    abstract fun applyMatrix(matrix: Matrix4): E

    fun rotateX(angle: Float): E {

        // rotate geometry around world x-axis
        val m1 = Matrix4()
        m1.makeRotationX(angle)
        this.applyMatrix(m1)

        return this as E

    }

    fun rotateY(angle: Float): E {

        // rotate geometry around world y-axis
        val m1 = Matrix4()
        m1.makeRotationY(angle)
        this.applyMatrix(m1)

        return this as E

    }

    fun rotateZ(angle: Float): E {

        // rotate geometry around world z-axis
        val m1 = Matrix4()
        m1.makeRotationZ(angle)
        this.applyMatrix(m1)

        return this as E

    }

    fun translate(x: Float, y: Float, z: Float): E {

        // translate geometry
        val m1 = Matrix4()
        m1.makeTranslation(x, y, z)
        this.applyMatrix(m1)

        return this as E

    }

    fun scale(x: Float, y: Float, z: Float): E {

        // scale geometry
        val m1 = Matrix4()
        m1.makeScale(x, y, z)
        this.applyMatrix(m1)

        return this as E

    }

    fun lookAt(vector: Vector3): E {
        val obj = Object3D()
        obj.lookAt(vector)
        obj.updateMatrix()
        this.applyMatrix(obj.matrix)

        return this as E
    }

    fun center(): E {
        val offset = Vector3()

        this.computeBoundingBox();
        this.boundingBox!!.getCenter(offset).negate()

        this.translate(offset.x, offset.y, offset.z)

        return this as E

    }

    fun normalize(): E {
        this.computeBoundingSphere()

        val center = this.boundingSphere!!.center
        val radius = this.boundingSphere!!.radius

        val s = if (radius == 0.toFloat()) 1.toFloat() else 1.toFloat() / radius

        val matrix = Matrix4()
        matrix.set(
            s, 0.toFloat(), 0.toFloat(), -s * center.x,
            0.toFloat(), s, 0.toFloat(), -s * center.y,
            0.toFloat(), 0.toFloat(), s, -s * center.z,
            0.toFloat(), 0.toFloat(), 0.toFloat(), 1.toFloat()
        );

        this.applyMatrix(matrix)

        return this as E
    }


    abstract fun computeBoundingBox()

    abstract fun computeBoundingSphere()

}
